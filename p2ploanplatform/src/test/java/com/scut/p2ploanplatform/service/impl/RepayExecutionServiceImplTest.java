package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.AccountTypeEnum;
import com.scut.p2ploanplatform.enums.P2pAccountStatusEnum;
import com.scut.p2ploanplatform.service.*;
import com.scut.p2ploanplatform.vo.RepayExecutionResultVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;
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
    private P2pAccountService p2pAccountService;

    private LoanApplication application;
    private Purchase purchase;
    private List<RepayPlan> repayPlans = new LinkedList<>();
    private List<RepayExecutionResultVo> repayResult;

    @Test
    // NON TRANSACTIONAL (access database via multiple sql session)
    public void doRepayTest() throws Exception {
        // inserting simulated users
        assertEquals(1, userService.insertUser("12345", 1, "123456", "13000000000", "440000000000000000", "111111111123", "郑优秀", "滑稽理工大学养猪场"));
        assertEquals(1, userService.insertUser("12346", 1, "123456", "13000000001", "440000000000000001", "111111111124", "郑成功", "双鸭山大学"));
        assertEquals(1, userService.insertUser("12347", 1, "123456", "13000000002", "440000000000000002", "111111111125", "郑龟龟", "滑稽理工大学养猪场"));
        p2pAccountService.addP2pAccount("111111111123", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode());
        p2pAccountService.addP2pAccount("111111111124", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode());
        p2pAccountService.addP2pAccount("111111111125", "123456", BigDecimal.valueOf(1000000, 2), P2pAccountStatusEnum.ACTIVE.getCode(), AccountTypeEnum.NORMAL.getCode());

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
        // todo: auto adding plan
        for (int i = -1; i <= 1; i++) {
            Date today = new Date(new Date().getTime() / 86400000 * 86400000);
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.MONTH, i);
            Date newDate = c.getTime();
            RepayPlan plan = repayService.insertPlan(purchase.getPurchaseId(), newDate, BigDecimal.valueOf(233.33));
            assertNotNull(plan);
            repayPlans.add(plan);
        }

        RepayExecutionServiceImpl.setThirdPartyApiUrl("http://localhost:" + port + "/third_party/");
        repayResult = repayExecutionService.doRepay();
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
        }
        for (RepayPlan plan : repayPlans) {
            databaseHelper.deleteRepayPlan(plan.getPlanId());
        }
    }

    // adding all delete functions
    @Mapper
    @Repository
    interface DatabaseHelper {
        @Delete("DELETE FROM `p2p`.`p2p_account` WHERE `third_party_id` = #{value}")
        int deleteP2pAccount(String id);
        @Delete("DELETE FROM `p2p`.`purchase` WHERE `purchase_id` = #{value}")
        int deletePurchase(Integer id);
        @Delete("DELETE FROM `p2p`.`repay_plan` WHERE `plan_id` = #{value}")
        int deleteRepayPlan(String id);
    }
}
