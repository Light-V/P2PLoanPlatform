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
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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

    private Purchase purchase;
    private LoanApplication application;

    @Test
    public void doRepayTest() throws Exception {
        // inserting simulated users
        assertEquals(1, userService.insertUser("12345", 1, "123456", "13000000000", "440000000000000000", "111111111123", "郑优秀", "滑稽理工大学养猪场"));
        assertEquals(1, userService.insertUser("12346", 1, "123456", "13000000001", "440000000000000001", "111111111124", "郑成功", "双鸭山大学"));
        assertEquals(1, userService.insertUser("12347", 1, "123456", "13000000002", "440000000000000002", "111111111125", "郑龟龟", "滑稽理工大学养猪场"));
        assertEquals(1, p2pAccountService.addP2pAccount("111111111123", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode()));
        assertEquals(1, p2pAccountService.addP2pAccount("111111111124", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode()));
        assertEquals(1, p2pAccountService.addP2pAccount("111111111125", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode()));
        databaseHelper.cleanRepayPlanTable();

        // fill loan application
        application = new LoanApplication();
        application.setAmount(BigDecimal.valueOf(1234.56));
        application.setBorrowerId("12345");
        application.setInterestRate(BigDecimal.valueOf(0.1234));
        application.setLoanMonth(12);
        application.setPurchaseDeadline(new Date(new Date().getTime() + 86400000));
        application.setTitle("在这无情的社会，只有金钱还有点温度");
        application.setStatus(0);
        assertTrue(loanApplicationService.addApplication(application));
        assertTrue(loanApplicationService.reviewPass(application.getApplicationId(), "12346"));
        // product subscribe
        purchase = purchaseService.subscribed("12347", application.getApplicationId());
        assertNotNull(purchase);
        List<RepayPlan> autoInsertPlans = repayService.findPlanByPurchaseId(purchase.getPurchaseId());
        assertEquals(12, autoInsertPlans.size());
        Date today = new Date(new Date().getTime() / 86400000 * 86400000);
        for (int i = 0; i < 12; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.MONTH, i+1);
            Date expectedDate = c.getTime();
            assertEquals(expectedDate, autoInsertPlans.get(i).getRepayDate());
            assertNull(autoInsertPlans.get(i).getRealRepayDate());
            assertEquals(RepayPlanStatus.SCHEDULED.getStatus().intValue(), autoInsertPlans.get(i).getStatus());
            // todo: fix this error : czy
//            assertEquals(BigDecimal.valueOf(115.58), autoInsertPlans.get(i).getAmount());
        }
        // deleting auto-insert plans
        assertEquals(12, databaseHelper.deleteRepayPlanByPurchaseId(purchase.getPurchaseId()));
        List<RepayPlan> repayPlans = new LinkedList<>();
        for (int i = -1; i <= 1; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.MONTH, i);
            Date newDate = c.getTime();
            RepayPlan plan = repayService.insertPlan(purchase.getPurchaseId(), newDate, BigDecimal.valueOf(233.33));
            assertNotNull(plan);
            repayPlans.add(plan);
        }

        if (p2pAccountService.getApiKey() == null) {
            p2pAccountService.generateApiKey();
        }
        ThirdPartyOperationInterface.setApiKey(p2pAccountService.getApiKey());
        ThirdPartyOperationInterface.setThirdPartyApiUrl("http://localhost:" + port + "/third_party/");
        List<RepayExecutionResultVo> repayResult = repayExecutionService.doRepay();
        assertEquals(2, repayResult.size());

        RepayExecutionResultVo overdueResult, normalResult;
        if (repayResult.get(0).getRepayPlanOld().getPlanId().equals(repayPlans.get(0).getPlanId())) {
            overdueResult = repayResult.get(0);
            normalResult = repayResult.get(1);
        } else {
            overdueResult = repayResult.get(1);
            normalResult = repayResult.get(0);
        }

        assertEquals(RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus().intValue(), overdueResult.getRepayPlanNew().getStatus());
        assertEquals(RepayPlanStatus.SUCCEEDED.getStatus().intValue(), normalResult.getRepayPlanNew().getStatus());
        assertEquals(RepayPlanStatus.SCHEDULED.getStatus().intValue(), normalResult.getRepayPlanOld().getStatus());
        assertEquals(RepayPlanStatus.OVERDUE.getStatus().intValue(), overdueResult.getRepayPlanOld().getStatus());
        assertEquals(BigDecimal.valueOf(233.33), overdueResult.getRepayPlanNew().getAmount());
        assertEquals(BigDecimal.valueOf(233.33), overdueResult.getRepayPlanOld().getAmount());
        assertEquals(BigDecimal.valueOf(233.33), normalResult.getRepayPlanNew().getAmount());
        assertEquals(BigDecimal.valueOf(233.33), normalResult.getRepayPlanOld().getAmount());
    }

    @Autowired
    private DatabaseHelper databaseHelper;

    @After
    public void tearDown() throws Exception {
        userService.deleteUser("12345");
        userService.deleteUser("12346");
        userService.deleteUser("12347");
        databaseHelper.deleteP2pAccount("111111111123");
        databaseHelper.deleteP2pAccount("111111111124");
        databaseHelper.deleteP2pAccount("111111111125");
        if (application != null) {
            loanApplicationService.deleteApplicationById(application.getApplicationId());
        }
        if (purchase != null) {
            databaseHelper.deletePurchase(purchase.getPurchaseId());
            databaseHelper.deleteRepayPlanByPurchaseId(purchase.getPurchaseId());
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
        @Delete("DELETE FROM `p2p`.`notice` WHERE `notice_id` = #{value}..")
        void deleteNotice(Integer id);
    }
}
