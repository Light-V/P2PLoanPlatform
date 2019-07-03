package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.AccountTypeEnum;
import com.scut.p2ploanplatform.enums.P2pAccountStatusEnum;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import com.scut.p2ploanplatform.service.*;
import com.scut.p2ploanplatform.utils.ThirdPartyOperationInterface;
import com.scut.p2ploanplatform.vo.RepayExecutionResultVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RepayExecutionServiceImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserService userService;

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private RepayService repayService;

    @Autowired
    private RepayExecutionService repayExecutionService;

    @Autowired
    private P2pAccountServiceImpl p2pAccountService;

    @Autowired
    private GuarantorService guarantorService;

    private List<Purchase> purchases = new LinkedList<>();
    private List<LoanApplication> applications = new LinkedList<>();
    private List<RepayExecutionResultVo> repayResults = new LinkedList<>();

    @Test
    public void doRepayTest() throws Exception {
        if (p2pAccountService.getApiKey() == null) {
            p2pAccountService.generateApiKey();
        }
        ThirdPartyOperationInterface.setApiKey(p2pAccountService.getApiKey());
        ThirdPartyOperationInterface.setThirdPartyApiUrl("http://localhost:" + port + "/third_party/");

        // inserting simulated users
        assertEquals(1, userService.insertUser("12345", 1, "123456", "13000000000", "440000000000000000", "111111111123", "郑优秀", "滑稽理工大学养猪场"));
        assertEquals(1, guarantorService.insertGuarantor("12346", "123456", "郑成功", "111111111124", 2));
        assertEquals(1, userService.insertUser("12347", 1, "123456", "13000000002", "440000000000000002", "111111111125", "郑龟龟", "滑稽理工大学养猪场"));
        assertEquals(1, userService.insertUser("12348", 1, "123456", "13000000003", "440000000000000003", "111111111126", "郑和", "滑稽理工老年大学"));
        assertEquals(1, guarantorService.insertGuarantor("12349", "123456", "桥本环奈", "111111111126", 2));

        assertEquals(1, p2pAccountService.addP2pAccount("111111111123", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode()));
        assertEquals(1, p2pAccountService.addP2pAccount("111111111124", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode()));
        assertEquals(1, p2pAccountService.addP2pAccount("111111111125", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode()));
        assertEquals(1, p2pAccountService.addP2pAccount("111111111126", "123456", BigDecimal.ONE, P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode()));
        databaseHelper.cleanRepayPlanTable();

        // fill loan application
        // 正常: 贷款人 -> 投资者
        LoanApplication application1 = new LoanApplication();
        // 贷款人划扣失败，由担保人划扣：担保人 -> 投资者
        LoanApplication application2 = new LoanApplication();
        // 担保人预垫付： 贷款人 -> 担保人
        LoanApplication application3 = new LoanApplication();
        // 谁都没钱，投资者难受的一匹
        LoanApplication application4 = new LoanApplication();
        application1.setAmount(BigDecimal.valueOf(1234.56));
        application2.setAmount(BigDecimal.valueOf(0.12));
        application3.setAmount(BigDecimal.valueOf(1234.56));
        application4.setAmount(BigDecimal.valueOf(1.23));

        application1.setBorrowerId("12345");
        application2.setBorrowerId("12348");
        application3.setBorrowerId("12345");
        application4.setBorrowerId("12348");

        application1.setInterestRate(BigDecimal.valueOf(0.1234));
        application2.setInterestRate(BigDecimal.valueOf(0.1234));
        application3.setInterestRate(BigDecimal.valueOf(0.1234));
        application4.setInterestRate(BigDecimal.valueOf(0.1234));

        application1.setLoanMonth(12);
        application2.setLoanMonth(1);
        application3.setLoanMonth(2);
        application4.setLoanMonth(6);

        application1.setPurchaseDeadline(new Date(new Date().getTime() + 86400000));
        application2.setPurchaseDeadline(new Date(new Date().getTime() + 86400000));
        application3.setPurchaseDeadline(new Date(new Date().getTime() + 86400000));
        application4.setPurchaseDeadline(new Date(new Date().getTime() + 86400000));

        application1.setTitle("在这无情的社会，只有金钱还有点温度");
        application2.setTitle("没钱也快活");
        application3.setTitle("我很可爱，给我打钱");
        application4.setTitle("打工是不可能打工的");

        application1.setStatus(0);
        application2.setStatus(0);
        application3.setStatus(0);
        application4.setStatus(0);

        assertTrue(loanApplicationService.addApplication(application1));
        assertTrue(loanApplicationService.addApplication(application2));
        assertTrue(loanApplicationService.addApplication(application3));
        assertTrue(loanApplicationService.addApplication(application4));

        assertTrue(loanApplicationService.reviewPass(application1.getApplicationId(), "12346"));
        assertTrue(loanApplicationService.reviewPass(application2.getApplicationId(), "12346"));
        assertTrue(loanApplicationService.reviewPass(application3.getApplicationId(), "12346"));
        assertTrue(loanApplicationService.reviewPass(application4.getApplicationId(), "12349"));

        applications.add(application1);
        applications.add(application2);
        applications.add(application3);
        applications.add(application4);

        // product subscribe
        Purchase purchase1 = purchaseService.subscribed("12347", application1.getApplicationId(), "123456");
        Purchase purchase2 = purchaseService.subscribed("12347", application2.getApplicationId(), "123456");
        Purchase purchase3 = purchaseService.subscribed("12347", application3.getApplicationId(), "123456");
        Purchase purchase4 = purchaseService.subscribed("12347", application4.getApplicationId(), "123456");
        assertNotNull(purchase1);
        assertNotNull(purchase2);
        assertNotNull(purchase3);
        assertNotNull(purchase4);
        // checking auto-insert plans
        List<RepayPlan> autoInsertPlans = repayService.findPlanByPurchaseId(purchase1.getPurchaseId());
        assertEquals(12, autoInsertPlans.size());
        Date today = new Date(new Date().getTime() / 86400000 * 86400000);
        for (int i = 0; i < 12; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.MONTH, i + 1);
            Date expectedDate = c.getTime();
            assertEquals(expectedDate, autoInsertPlans.get(i).getRepayDate());
            assertNull(autoInsertPlans.get(i).getRealRepayDate());
            assertEquals(RepayPlanStatus.SCHEDULED.getStatus(), autoInsertPlans.get(i).getStatus());
//            assertEquals(BigDecimal.valueOf(115.58), autoInsertPlans.get(i).getAmount());
        }
        // deleting auto-insert plans
        assertEquals(12, databaseHelper.deleteRepayPlanByPurchaseId(purchase1.getPurchaseId()));
        databaseHelper.cleanRepayPlanTable();
        purchases.add(purchase1);
        purchases.add(purchase2);
        purchases.add(purchase3);
        purchases.add(purchase4);
        List<RepayPlan> repayPlans = new LinkedList<>();
        for (int i = -1; i <= 1; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.MONTH, i);
            Date newDate = c.getTime();
            RepayPlan plan = repayService.insertPlan(purchase1.getPurchaseId(), newDate, BigDecimal.valueOf(233.33));
            assertNotNull(plan);
            repayPlans.add(plan);
        }
        repayPlans.add(repayService.insertPlan(purchase2.getPurchaseId(), today, BigDecimal.valueOf(123.45)));
        // set guarantor paid for application3
        RepayPlan plan = repayService.insertPlan(purchase3.getPurchaseId(), today, BigDecimal.valueOf(123.45));
        repayService.updateRepayPlan(plan.getPlanId(), RepayPlanStatus.GUARANTOR_PAID_ADVANCE, null);
        repayPlans.add(repayService.findPlanById(plan.getPlanId()));
        repayPlans.add(repayService.insertPlan(purchase4.getPurchaseId(), today, BigDecimal.valueOf(543.21)));

        assertEquals(6, repayPlans.size());


        // ***** DO REPAY HERE *****
        repayResults = repayExecutionService.doRepay();

        assertEquals(5, repayResults.size());

        Map<RepayPlan, RepayExecutionResultVo> results = new HashMap<>();
        for (RepayPlan repayPlan : repayPlans) {
            for (RepayExecutionResultVo repayExecutionResultVo : repayResults) {
                if (repayPlan.getPlanId().equals(repayExecutionResultVo.getRepayPlanOld().getPlanId())) {
                    results.put(repayPlan, repayExecutionResultVo);
                    break;
                }
            }
        }

        // VALIDATING RESULTS
        RepayExecutionResultVo overdueResult = results.get(repayPlans.get(0));
        RepayExecutionResultVo normalResult = results.get(repayPlans.get(1));
        RepayExecutionResultVo guarantorPaidResult = results.get(repayPlans.get(3));
        RepayExecutionResultVo borrowerPaidToGuarantorResult = results.get(repayPlans.get(4));
        RepayExecutionResultVo ggResult = results.get(repayPlans.get(5));

        assertEquals(RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus(), overdueResult.getRepayPlanNew().getStatus());
        assertEquals(RepayPlanStatus.OVERDUE.getStatus(), overdueResult.getRepayPlanOld().getStatus());
        assertEquals(BigDecimal.valueOf(233.33), overdueResult.getRepayPlanNew().getAmount());
        assertEquals(BigDecimal.valueOf(233.33), overdueResult.getRepayPlanOld().getAmount());
        assertEquals(0, overdueResult.getBorrowerTransferResult().getCode());
        assertNull(overdueResult.getGuarantorTransferResult());
        assertNull(overdueResult.getRepayPlanOld().getRealRepayDate());
        assertNotNull(overdueResult.getRepayPlanNew().getRealRepayDate());
        assertEquals(new Date(new Date().getTime() / 86400000 * 86400000), overdueResult.getRepayPlanNew().getRealRepayDate());

        assertEquals(RepayPlanStatus.SUCCEEDED.getStatus(), normalResult.getRepayPlanNew().getStatus());
        assertEquals(RepayPlanStatus.SCHEDULED.getStatus(), normalResult.getRepayPlanOld().getStatus());
        assertEquals(0, normalResult.getBorrowerTransferResult().getCode());
        assertNull(normalResult.getGuarantorTransferResult());
        assertNull(overdueResult.getRepayPlanOld().getRealRepayDate());
        assertNotNull(overdueResult.getRepayPlanNew().getRealRepayDate());

        assertEquals(RepayPlanStatus.SCHEDULED.getStatus(), guarantorPaidResult.getRepayPlanOld().getStatus());
        assertEquals(RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus(), guarantorPaidResult.getRepayPlanNew().getStatus());
        assertEquals(0, guarantorPaidResult.getGuarantorTransferResult().getCode());
        assertNotEquals(0, guarantorPaidResult.getBorrowerTransferResult().getCode());
        assertNull(guarantorPaidResult.getRepayPlanOld().getRealRepayDate());
        assertNull(guarantorPaidResult.getRepayPlanNew().getRealRepayDate());

        assertEquals(RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus(), borrowerPaidToGuarantorResult.getRepayPlanOld().getStatus());
        assertEquals(RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus(), borrowerPaidToGuarantorResult.getRepayPlanNew().getStatus());
        assertEquals(0, borrowerPaidToGuarantorResult.getBorrowerTransferResult().getCode());
        assertNull(borrowerPaidToGuarantorResult.getGuarantorTransferResult());
        assertNull(borrowerPaidToGuarantorResult.getRepayPlanOld().getRealRepayDate());
        assertNotNull(borrowerPaidToGuarantorResult.getRepayPlanNew().getRealRepayDate());

        assertEquals(RepayPlanStatus.SCHEDULED.getStatus(), ggResult.getRepayPlanOld().getStatus());
        assertEquals(RepayPlanStatus.OVERDUE.getStatus(), ggResult.getRepayPlanNew().getStatus());
        assertNotEquals(0, ggResult.getBorrowerTransferResult().getCode());
        assertNotEquals(0, ggResult.getGuarantorTransferResult().getCode());
        assertNull(ggResult.getRepayPlanOld().getRealRepayDate());
        assertNull(ggResult.getRepayPlanNew().getRealRepayDate());

    }

    @Autowired
    private DatabaseHelper databaseHelper;

    @After
    public void tearDown() throws Exception {
        userService.deleteUser("12345");
        databaseHelper.deleteGuarantor("12346");
        userService.deleteUser("12347");
        userService.deleteUser("12348");
        databaseHelper.deleteGuarantor("12349");
        databaseHelper.deleteP2pAccount("111111111123");
        databaseHelper.deleteP2pAccount("111111111124");
        databaseHelper.deleteP2pAccount("111111111125");
        databaseHelper.deleteP2pAccount("111111111126");
        for (LoanApplication application : applications)
            if (application != null) {
                loanApplicationService.deleteApplicationById(application.getApplicationId());
            }
        for (Purchase purchase : purchases)
            if (purchase != null) {
                databaseHelper.deletePurchase(purchase.getPurchaseId());
                databaseHelper.deleteRepayPlanByPurchaseId(purchase.getPurchaseId());
            }
        if (repayResults != null) {
            for (RepayExecutionResultVo plan : repayResults) {
                if (plan.getBorrowerNotice() != null)
                    databaseHelper.deleteNotice(plan.getBorrowerNotice().getNoticeId());
                if (plan.getGuarantorNotice() != null)
                    databaseHelper.deleteNotice(plan.getGuarantorNotice().getNoticeId());
                if (plan.getInvestorNotice() != null)
                    databaseHelper.deleteNotice(plan.getInvestorNotice().getNoticeId());
            }
        }
    }

    // adding all delete functions
    @SuppressWarnings("UnusedReturnValue")
    @Mapper
    @Repository
    public interface DatabaseHelper {
        @Delete("DELETE FROM `p2p`.`p2p_account` WHERE `third_party_id` = #{value}")
        int deleteP2pAccount(String id);

        @Delete("DELETE FROM `p2p`.`purchase` WHERE `purchase_id` = #{value}")
        int deletePurchase(Integer id);

        @Delete("DELETE FROM `p2p`.`repay_plan`")
        void cleanRepayPlanTable();

        @Delete("DELETE FROM `p2p`.`repay_plan` WHERE `purchase_id` = #{value}")
        int deleteRepayPlanByPurchaseId(Integer id);

        @Delete("DELETE FROM `p2p`.`notice` WHERE `notice_id` = #{value}")
        void deleteNotice(Integer id);

        @Delete("DELETE FROM `p2p`.`guarantor` WHERE `guarantor_id` = #{value}")
        void deleteGuarantor(String id);
    }
}
