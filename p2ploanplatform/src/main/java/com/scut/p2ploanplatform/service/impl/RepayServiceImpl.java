package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.RepayPlanDao;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import com.scut.p2ploanplatform.service.*;
import com.scut.p2ploanplatform.utils.AutoTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@SuppressWarnings({"deprecation", "RedundantSuppression"})
public class RepayServiceImpl implements RepayService {
    private static final String NOTICE_TITLE = "喵了个咪";
    private Logger logger = LoggerFactory.getLogger(RepayServiceImpl.class);
    private boolean isColdStart = true;

    private RepayPlanDao repayPlanDao;
    private PurchaseService purchaseService;
    private NoticeService noticeService;
    private WaterBillService waterBillService;
    private UserService userService;
    private P2pAccountService p2pAccountService;

    @Autowired
    public void setRepayPlanDao(RepayPlanDao repayPlanDao) {
        this.repayPlanDao = repayPlanDao;
    }

    @Autowired
    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

//    @Autowired
//    public void setWaterBillService(WaterBillService waterBillService) {
//        this.waterBillService = waterBillService;
//    }

    @Autowired
    public void setP2pAccountService(P2pAccountService p2pAccountService) {
        this.p2pAccountService = p2pAccountService;
    }

    private Object[] getAutowiredMembers() {
        return new Object[] {repayPlanDao, purchaseService, noticeService, waterBillService, userService, p2pAccountService};
    }

    public RepayServiceImpl() throws Exception {
        new AutoTrigger(getClass().getDeclaredMethod("doRepay"), this, 8, 0, 0, true);
    }


    @Override
    public RepayPlan insertPlan(Integer purchaseId, Date repayDate, BigDecimal amount) throws SQLException, IllegalArgumentException {
        if (purchaseId == null || purchaseId <= 0)
            throw new IllegalArgumentException("invalid purchaseId, should be non null and positive");
        if (repayDate == null)
            throw new IllegalArgumentException("invalid repayDate, should be non null");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("invalid amount, should be non null and positive");

        RepayPlan plan = new RepayPlan();
        plan.setRepayDate(repayDate);
        plan.setAmount(amount);
        plan.setPlanId(UUID.randomUUID().toString().replace("-", ""));
        plan.setPurchaseId(purchaseId);
        if (repayDate.before(new Date())) {
            logger.warn("Trying to insert an overdue repay plan, check your code");
            plan.setStatus(RepayPlanStatus.OVERDUE.getStatus());
        } else {
            plan.setStatus(RepayPlanStatus.SCHEDULED.getStatus());
        }

        try {
            repayPlanDao.insertPlan(plan);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }

        return plan;
    }

    @Override
    public List<RepayPlan> findPlanByPurchaseId(Integer id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("invalid purchaseId, should be non null and positive");

        try {
            return repayPlanDao.findPlanByPurchaseId(id);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public RepayPlan findPlanById(String id) throws SQLException, IllegalArgumentException {
        if (id == null || id.length() != 32)
            throw new IllegalArgumentException("invalid planId, should be non null and has length of 32");

        try {
            return repayPlanDao.findPlanByPlanId(id);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRepayPlan(String id, RepayPlanStatus status, Date realRepayDate) throws SQLException, IllegalArgumentException {
        if (id == null || id.length() != 32)
            throw new IllegalArgumentException("invalid planId, should be non null and has length of 32");
        if (status == null)
            throw new IllegalArgumentException("status could not be null");
        if (realRepayDate != null && (status != RepayPlanStatus.SUCCEEDED && status != RepayPlanStatus.OVERDUE_SUCCEEDED))
            throw new IllegalArgumentException("invalid argument: realRepayDate and status, status must be SUCCEEDED if realRepayDate is set");
        try {
            RepayPlan plan = repayPlanDao.findPlanByPlanId(id);

            // optional: add expire check here
            plan.setStatus(status.getStatus());
            plan.setRealRepayDate(realRepayDate);
            repayPlanDao.updatePlan(plan);
        }
        catch (IllegalArgumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public synchronized void doRepay() {
        logger.info("Repay thread ready, waiting beans set");
        while (true) {
            Object[] autowiredValues = getAutowiredMembers();
            boolean hasNullValue = false;
            for(Object i : autowiredValues) {
                if (i == null) {
                    hasNullValue = true;
                    break;
                }
            }
            if (!hasNullValue)
                break;
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ignore) {
                return;
            }
        }
        logger.info("Starting daily repay process");

        if (isColdStart) {
            isColdStart = false;
            logger.info("Updating plan status (from cold start)");
            int rowsAffected = repayPlanDao.updatePlanStatus();
            logger.info("Completed query, " + rowsAffected + " rows affected");
        }

        List<RepayPlan> plans = repayPlanDao.findAllUnpaidPlan();
        logger.info("Completed query, got " + plans.size() + " results");

        for (RepayPlan plan : plans) {
            // 还款流程
            try {
                // 获取认购信息
                Purchase purchase = purchaseService.showPurchaseById(plan.getPurchaseId());

                // 获取用户信息
                User borrower = userService.findUser(purchase.getBorrowerId());
                User investor = userService.findUser(purchase.getInvestorId());
                User guarantor = userService.findUser(purchase.getGuarantorId());

                // 转账
                boolean transactionSuccess;
                // 已由担保人代付，还款默认转到担保人账号，其余情况都转到投资者账号
                User payee = (plan.getStatus() == RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus()) ? guarantor : investor;
                User payer = borrower;
                String borrowerMessage, guarantorMessage = null, investorMessage = null;

                transactionSuccess = p2pAccountService.transfer(payer.getThirdPartyId(), payee.getThirdPartyId(), plan.getAmount());

                if (transactionSuccess) {
                    // 贷款人还款成功：贷款人 -> 投资者/担保人
                    plan.setRealRepayDate(new Date());
                    borrowerMessage = String.format("尊敬的 %s 用户，您本月贷款还款成功，已成功还款 %s 元", borrower.getName(), plan.getAmount().toString());
                    if (payee == guarantor) { // WARN: weak reference equal, todo: implement equals()
                        plan.setStatus(RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus());
                        guarantorMessage = String.format("尊敬的 %s 用户，您担保的贷款人 %s 本月已完成还款，还款金额已经转入您的第三方账号中", guarantor.getName(), borrower.getName());
                    } else {
                        plan.setStatus((plan.getStatus() == RepayPlanStatus.SCHEDULED.getStatus()) ? RepayPlanStatus.SUCCEEDED.getStatus() : RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus());
                        investorMessage = String.format("尊敬的 %s 用户，您本月投资的贷款已完成还款，已成功收款 %s 元", investor.getName(), plan.getAmount().toString());
                    }
                } else {
                    // 贷款人还款失败，执行垫付：担保人 -> 投资者
                    payer = guarantor;
                    transactionSuccess = p2pAccountService.transfer(payer.getThirdPartyId(), payee.getThirdPartyId(), plan.getAmount());
                    borrowerMessage = String.format("尊敬的 %s 用户，您的贷款本月还款失败，请检查第三方账号中金额，确保在系统重试还款前，拥有足够金额进行划扣", borrower.getName());

                    if (transactionSuccess) {
                        // 担保人代付成功
                        plan.setStatus(RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus());
                        investorMessage = String.format("尊敬的 %s 用户，您投资的贷款已完成本月还款，已成功收款 %s 元", borrower.getName(), plan.getAmount().toString());
                    } else {
                        // 支付失败，投资方money全部木大
                        // 只对首次失败通知投资方
                        if (plan.getStatus() != RepayPlanStatus.OVERDUE.getStatus()) {
                            investorMessage = String.format("尊敬的 %s 用户，您投资贷款本月未及时还款，系统已经为您执行逾期流程", investor.getName());
                            plan.setStatus(RepayPlanStatus.OVERDUE.getStatus());
                        }
                    }
                }
                repayPlanDao.updatePlan(plan);
                waterBillService.addRepayRecord(plan.getPlanId(), plan.getPurchaseId(), payer.getUserId(), payee.getUserId(), plan.getRepayDate(), plan.getAmount());

                if (borrowerMessage != null)
                    noticeService.sendNotice(borrower.getUserId(), NOTICE_TITLE, borrowerMessage);
                if (guarantorMessage != null)
                    noticeService.sendNotice(guarantor.getUserId(), NOTICE_TITLE, guarantorMessage);
                if (investorMessage != null)
                    noticeService.sendNotice(investor.getUserId(), NOTICE_TITLE, investorMessage);
            }
            catch (Exception e) {
                logger.error("Exception caught while executing repay routine in plan id: " + plan.getPlanId(), e);
            }

        }
    }
}
