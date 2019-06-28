package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RepayExecutionServiceImplTest {

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

    @Test
    @Transactional
    public void doRepayTest() throws Exception {
        // inserting simulated users
        assertEquals(1, userService.insertUser("12345", 1, "123456", "13000000000", "440000000000000000", "123", "郑优秀", "滑稽理工大学养猪场"));
        assertEquals(1, userService.insertUser("12346", 1, "123456", "13000000001", "440000000000000001", "124", "郑成功", "双鸭山大学"));
        assertEquals(1, userService.insertUser("12347", 1, "123456", "13000000002", "440000000000000002", "125", "郑龟龟", "滑稽理工大学养猪场"));
        // fill loan application
        LoanApplication application = new LoanApplication();
        application.setAmount(BigDecimal.valueOf(1234.56));
        application.setBorrowerId("12345");
//        application.setGuarantorId("12346");
        application.setInterestRate(BigDecimal.valueOf(0.1234));
        application.setLoanMonth(12);
        application.setPurchaseDeadline(new Date(new Date().getTime() + 86400000));
        application.setTitle("在这无情的社会，只有金钱还有点温度");
        application.setStatus(0);
        assertTrue(loanApplicationService.addApplication(application));
        loanApplicationService.reviewPass(application.getApplicationId(), "12346");
//        application.setStatus(LoanStatus.REVIEWED_PASSED.getStatus());
        // product subscribe
        Purchase purchase = purchaseService.subscribed("12347", application.getApplicationId());
        assertNotNull(purchase);
        // todo: auto adding plan
        for (int i = -1; i <= 1; i++) {
            Date today = new Date(new Date().getTime() / 86400000 * 86400000);
            Calendar c = Calendar.getInstance();
            c.setTime(today);
            c.add(Calendar.MONTH, i);
            Date newDate = c.getTime();
            assertNotNull(repayService.insertPlan(purchase.getPurchaseId(), newDate, BigDecimal.valueOf(233.33)));
        }

        repayExecutionService.doRepay();
    }
}