package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.RepayPlanDao;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import com.scut.p2ploanplatform.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
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
//    private WaterBillService waterBillService;
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

    public RepayServiceImpl() throws Exception {
        RepayAutoTrigger trigger = new RepayAutoTrigger(this, getClass().getDeclaredMethod("doRepay"));
        Thread thread = new Thread(trigger);
        thread.setDaemon(true);
        thread.setName("RepayThread");
        thread.start();
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
        // waiting autowired value set
        while (repayPlanDao == null || purchaseService == null) {
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
                boolean transactionSuccess = p2pAccountService.pay(borrower.getThirdPartyId(), plan.getAmount());
                if (transactionSuccess) {
                    plan.setRealRepayDate(new Date());
                    //todo: modify this hard-coded message to configuration file
                    noticeService.sendNotice(borrower.getUserId(), NOTICE_TITLE,
                            String.format("尊敬的 %s 用户，您本月贷款还款成功，已成功还款 %s 元", borrower.getName(), plan.getAmount().toString()));
                    if (plan.getStatus() == RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus()) {
                        // 已由担保人代付，还款默认转到担保人账号
                        p2pAccountService.income(guarantor.getThirdPartyId(), plan.getAmount());

                        plan.setStatus(RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus());

                    noticeService.sendNotice(guarantor.getUserId(), NOTICE_TITLE,
                            String.format("尊敬的 %s 用户，您担保的贷款人 %s 本月已完成还款，还款金额已经转入您的第三方账号中", guarantor.getName(), borrower.getName()));
                    } else {
                        // 其余情况都转到投资者账号
                        p2pAccountService.income(investor.getThirdPartyId(), plan.getAmount());

                        if (plan.getStatus() == RepayPlanStatus.SCHEDULED.getStatus())
                            plan.setStatus(RepayPlanStatus.SUCCEEDED.getStatus());
                        else
                            plan.setStatus(RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus());
                        //todo: add water bill: borrower -> investor

                        noticeService.sendNotice(investor.getUserId(), NOTICE_TITLE,
                                String.format("尊敬的 %s 用户，您本月投资的贷款已完成还款，已成功收款 %s 元", investor.getName(), plan.getAmount().toString()));

                    }
                } else {
                    // transaction failed: borrower -> investor

                    transactionSuccess = p2pAccountService.pay(guarantor.getThirdPartyId(), plan.getAmount());
                    noticeService.sendNotice(borrower.getUserId(), NOTICE_TITLE,
                            String.format("尊敬的 %s 用户，您的贷款本月还款失败，请检查第三方账号中金额，确保在系统重试还款前，拥有足够金额进行划扣", borrower.getName()));

                    if (transactionSuccess) {
                        p2pAccountService.income(investor.getThirdPartyId(), plan.getAmount());

                        // 担保人代付成功
                        plan.setStatus(RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus());

                        noticeService.sendNotice(investor.getUserId(), NOTICE_TITLE,
                                String.format("尊敬的 %s 用户，您投资的贷款已完成本月还款，已成功收款 %s 元", borrower.getName(), plan.getAmount().toString()));
                        // todo: add water bill: guarantor -> investor
                    } else {
                        // 支付失败，投资方money全部木大

                        // 只对首次失败通知投资方
                        if (plan.getStatus() != RepayPlanStatus.OVERDUE.getStatus()) {
                            noticeService.sendNotice(investor.getUserId(), NOTICE_TITLE,
                                    String.format("尊敬的 %s 用户，您投资贷款本月未及时还款，系统已经为您执行逾期流程", investor.getName()));
                            plan.setStatus(RepayPlanStatus.OVERDUE.getStatus());
                        }
                    }
                }
                repayPlanDao.updatePlan(plan);
            }
            catch (Exception e) {
                logger.error("Exception caught while executing repay routine in plan id: " + plan.getPlanId(), e);
            }

        }
    }
}


@SuppressWarnings("WeakerAccess")
class RepayAutoTrigger implements Runnable {
    private Logger logger = LoggerFactory.getLogger(RepayAutoTrigger.class);
    // 这里定义了每天触发还款任务的时间
    // todo: modify this hard-coded schedule time to global config in Iteration 2
    public static final int REPAY_HOUR = 8;
    public static final int REPAY_MINUTE = 0;
    public static final int REPAY_SECOND = 0;

    private Method callbackMethod;
    private Object invokeObject;

    RepayAutoTrigger(Object invokeObject, Method callbackMethod) {
        this.invokeObject = invokeObject;
        this.callbackMethod = callbackMethod;
    }

    @Override
    public void run() {
        try {
            logger.info("Started repay trigger thread");
            long repayTimeOfDay = (REPAY_SECOND + (60 * (60 * REPAY_HOUR + REPAY_MINUTE))) * 1000;
            long nextDateTime = (new Date().getTime() + 86399999) / 86400000;
            long nextRepayTimestamp = nextDateTime * 86400000 + repayTimeOfDay;

            //noinspection InfiniteLoopStatement
            while (true) {
                callbackMethod.invoke(invokeObject);

                nextRepayTimestamp += 86400000;
                long sleepTimeForNextDay = (nextRepayTimestamp - new Date().getTime());
                if (sleepTimeForNextDay > 0)
                    Thread.sleep(sleepTimeForNextDay);
            }
        }catch (Exception e) {
            logger.error(e.toString());

            // retry
            Thread thread = new Thread(() -> {
                try {
                    logger.warn("Unhandled exception caught in auto trigger thread, restarting in 1 hours");
                    Thread.sleep(3600000);
                    Thread restartThread = new Thread(new RepayAutoTrigger(this.invokeObject, this.callbackMethod));
                    restartThread.setDaemon(true);
                    restartThread.setName("RepayThread");
                    restartThread.start();
                }
                catch (InterruptedException ignore) {}
            });

            thread.setDaemon(true);
            thread.setName("RepayThread");
            thread.start();
        }
    }
}