package com.scut.p2ploanplatform.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scut.p2ploanplatform.dao.RepayPlanDao;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import com.scut.p2ploanplatform.service.*;
import com.scut.p2ploanplatform.utils.AutoTrigger;
import com.scut.p2ploanplatform.utils.AutowireField;
import com.scut.p2ploanplatform.vo.RepayExecutionResultVo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import java.security.MessageDigest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class RepayExecutionServiceImpl implements RepayExecutionService {
    private static final String NOTICE_TITLE = "喵了个咪";
    private boolean isColdStart = true;

    private static String THIRD_PARTY_API_URL = "http://localhost:8080/third_party/";

    public static String getThirdPartyApiUrl() {
        return THIRD_PARTY_API_URL;
    }

    public static void setThirdPartyApiUrl(String thirdPartyApiUrl) {
        if (!thirdPartyApiUrl.endsWith("/"))
            thirdPartyApiUrl = thirdPartyApiUrl + "/";
        THIRD_PARTY_API_URL = thirdPartyApiUrl;
    }

    @AutowireField
    private PurchaseService purchaseService;
    @AutowireField
    private NoticeService noticeService;
    @AutowireField
    private WaterBillService waterBillService;
    @AutowireField
    private UserService userService;
    @AutowireField
    private RepayPlanDao repayPlanDao;

    @AutowireField
    private P2pAccountServiceImpl p2pAccountService;

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

    @Autowired
    public void setWaterBillService(WaterBillService waterBillService) {
        this.waterBillService = waterBillService;
    }

    @Autowired
    public void setP2pAccountService(P2pAccountServiceImpl p2pAccountService) {
        this.p2pAccountService = p2pAccountService;
    }

    public RepayExecutionServiceImpl() throws Exception {
        new AutoTrigger(getClass().getDeclaredMethod("doRepay"), this, 1, 0, 0, true);
    }


    @Data
    public static class ThirdPartyTransferResult {
        private Integer code;
        private String msg;
    }

    private static String API_KEY = null;
//    private static final String API_SECRET_KEY = "";

    private String computeThirdPartyParamSign(String paramString) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] sha256 = digest.digest(paramString.getBytes());
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : sha256) {
            String hex = Integer.toHexString(b & 0xff);
            if (hex.length() == 1)
                stringBuilder.append('0');
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }

    private ThirdPartyTransferResult sendTransferRequest(String payerId, String payeeId, BigDecimal amount) throws Exception{
        String url = THIRD_PARTY_API_URL + "transfer";
        List<NameValuePair> queryParams = new LinkedList<>();
        queryParams.add(new BasicNameValuePair("payer_id", payerId));
        queryParams.add(new BasicNameValuePair("payee_id", payeeId));
        queryParams.add(new BasicNameValuePair("amount", amount.toString()));
        queryParams.add(new BasicNameValuePair("api_key", API_KEY));
        queryParams.add(new BasicNameValuePair("signature", computeThirdPartyParamSign(payerId + payeeId + amount.toString() + API_KEY)));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder(url).addParameters(queryParams).build();

        HttpPut httpPut = new HttpPut(uri);
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPut)) {
            HttpEntity entity = httpResponse.getEntity();
            String content = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            ThirdPartyTransferResult result = objectMapper.readValue(content, ThirdPartyTransferResult.class);
            log.info(content);
            return result;
        }
    }

    private <T extends Serializable> T deepCopy(T object) throws Exception {
        return (T)SerializationUtils.clone(object);
    }

    @Override
    public synchronized List<RepayExecutionResultVo> doRepay() {
        log.info("Starting daily repay process");
        List<RepayExecutionResultVo> results = new LinkedList<>();

        if (isColdStart) {
            isColdStart = false;
            log.debug("Updating plan status (from cold start)");
            int rowsAffected = repayPlanDao.updatePlanStatus();
            log.debug("Completed query, " + rowsAffected + " rows affected");
        }

        if (API_KEY == null) {
            log.debug("requiring API_KEY");
            API_KEY = p2pAccountService.generateApiKey();
            log.debug("done, got API_KEY = " + API_KEY);
        }

        log.debug("Querying unpaid plans");
        List<RepayPlan> plans = repayPlanDao.findAllUnpaidPlan();
        log.debug("Completed query, got " + plans.size() + " results");

        for (RepayPlan plan : plans) {
            // 还款流程
            try {
                RepayExecutionResultVo result = new RepayExecutionResultVo();
                result.setRepayPlanOld(deepCopy(plan));
                // 获取认购信息
                Purchase purchase = purchaseService.showPurchaseById(plan.getPurchaseId());

                // 获取用户信息
                User borrower = userService.findUser(purchase.getBorrowerId());
                User investor = userService.findUser(purchase.getInvestorId());
                User guarantor = userService.findUser(purchase.getGuarantorId());

                // 转账
                ThirdPartyTransferResult transferResult;
                // 已由担保人代付，还款默认转到担保人账号，其余情况都转到投资者账号
                User payee = (plan.getStatus() == RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus()) ? guarantor : investor;
                User payer = borrower;
                String borrowerMessage = null, guarantorMessage = null, investorMessage = null;

                transferResult = sendTransferRequest(payer.getThirdPartyId(), payee.getThirdPartyId(), plan.getAmount());
                result.setBorrowerTransferResult(transferResult);

                if (transferResult.getCode() == 0) {
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
                    if (plan.getStatus() != RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus()) {
                        // 贷款人还款失败，执行垫付：担保人 -> 投资者
                        payer = guarantor;
                        transferResult = sendTransferRequest(payer.getThirdPartyId(), payee.getThirdPartyId(), plan.getAmount());
                        result.setGuarantorTransferResult(transferResult);
                        borrowerMessage = String.format("尊敬的 %s 用户，您的贷款本月还款失败，请检查第三方账号中金额，确保在系统重试还款前，拥有足够金额进行划扣", borrower.getName());

                        if (transferResult.getCode() == 0) {
                            // 担保人代付成功
                            plan.setStatus(RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus());
                            investorMessage = String.format("尊敬的 %s 用户，您投资的贷款已完成本月还款，已成功收款 %s 元", borrower.getName(), plan.getAmount().toString());
                        } else {
                            // 支付失败，投资方money全部木大
                            // 只对首次失败通知投资方
                            if (plan.getStatus() != RepayPlanStatus.OVERDUE.getStatus()) {
                                investorMessage = String.format("尊敬的 %s 用户，您投资贷款本月未及时还款，系统已经为您执行逾期流程", investor.getName());
                                plan.setStatus(RepayPlanStatus.OVERDUE.getStatus());
                                // calling PurchaseService: set overdue status
                                purchaseService.purchaseOverdue(plan.getPurchaseId());
                            }
                        }
                    }

                    // todo: 28 天逾期后锁定银行卡
                }
                repayPlanDao.updatePlan(plan);
                waterBillService.addRepayRecord(plan.getPlanId(), plan.getPurchaseId(), payer.getUserId(), payee.getUserId(), plan.getRepayDate(), plan.getAmount());

                if (borrowerMessage != null)
                    result.setBorrowerNotice(noticeService.sendNotice(borrower.getUserId(), NOTICE_TITLE, borrowerMessage));
                if (guarantorMessage != null)
                    result.setGuarantorNotice(noticeService.sendNotice(guarantor.getUserId(), NOTICE_TITLE, guarantorMessage));
                if (investorMessage != null)
                    result.setInvestorNotice(noticeService.sendNotice(investor.getUserId(), NOTICE_TITLE, investorMessage));
                result.setRepayPlanNew(deepCopy(plan));

                results.add(result);
            } catch (Exception e) {
                log.error("Exception caught while executing repay routine in plan id: " + plan.getPlanId(), e);
            }

        }
        return results;
    }
}
