package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import com.scut.p2ploanplatform.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RepayServiceImplTest {

    @Autowired
    private RepayService repayService;

    @Test
    @Transactional
    public void insertPlanTest() {
        Date targetDate = new Date(new Date().getTime() / 86400000 * 86400000 + 86400000);
        try {
            RepayPlan plan = repayService.insertPlan(123, targetDate, BigDecimal.valueOf(100, 2));
            assertNotNull(plan);
            assertNotNull(plan.getPlanId());
            assertEquals(32, plan.getPlanId().length());
            assertEquals(RepayPlanStatus.SCHEDULED.getStatus(), Integer.valueOf(plan.getStatus()));
            assertEquals(Integer.valueOf(123), plan.getPurchaseId());
            assertEquals(targetDate, plan.getRepayDate());
            assertNull(plan.getRealRepayDate());
            assertEquals(BigDecimal.valueOf(100,2), plan.getAmount());

            try {
                repayService.insertPlan(null, null, null);
                fail();
            }
            catch (IllegalArgumentException ignore) {}

            try {
                repayService.insertPlan(-1,null,null);
                fail();
            }
            catch (IllegalArgumentException ignore) {}

            try {
                repayService.insertPlan(123, null, null);
                fail();
            }
            catch (IllegalArgumentException ignore) {}

            try {
                repayService.insertPlan(123, targetDate, null);
                fail();
            }
            catch (IllegalArgumentException ignore) {}

            try {
                repayService.insertPlan(123, targetDate, BigDecimal.valueOf(0,2));
                fail();
            }
            catch (IllegalArgumentException ignore) {}

            try {
                repayService.insertPlan(123, targetDate, BigDecimal.valueOf(-1,2));
                fail();
            }
            catch (IllegalArgumentException ignore) {}
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Transactional
    public void findPlanByPurchaseIdTest() {
        Date targetDate = new Date(new Date().getTime() + 86400000);

        try {
            repayService.insertPlan(1234, targetDate, BigDecimal.valueOf(123,2));
            repayService.insertPlan(12345, targetDate, BigDecimal.valueOf(123,2));
            repayService.insertPlan(1234, targetDate, BigDecimal.valueOf(1233,2));

            List<RepayPlan> plans = repayService.findPlanByPurchaseId(1234);
            assertEquals(2, plans.size());
            plans = repayService.findPlanByPurchaseId(12345);
            assertEquals(1, plans.size());
            plans = repayService.findPlanByPurchaseId(123456);
            assertEquals(0, plans.size());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Transactional
    public void findPlanByIdTest() {
        Date targetDate = new Date(new Date().getTime() + 86400000);

        try {
            RepayPlan plan = repayService.insertPlan(1234, targetDate, BigDecimal.valueOf(123,2));
            String planId = plan.getPlanId();
            plan = repayService.findPlanById(planId);
            assertNotNull(plan);
            plan = repayService.findPlanById(UUID.randomUUID().toString().replace("-", ""));
            assertNull(plan);
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Transactional
    public void updateRepayPlanTest() {
        Date targetDate = new Date(new Date().getTime() + 86400000);

        try {
            RepayPlan plan = repayService.insertPlan(233333, targetDate, BigDecimal.valueOf(123.45));
            try {
                repayService.updateRepayPlan(plan.getPlanId(), RepayPlanStatus.SCHEDULED, new Date());
                fail();
            }
            catch (IllegalArgumentException ignore) {}

            repayService.updateRepayPlan(plan.getPlanId(), RepayPlanStatus.SUCCEEDED, new Date());
            repayService.updateRepayPlan(plan.getPlanId(), RepayPlanStatus.OVERDUE_SUCCEEDED, new Date());
            RepayPlan actualPlan = repayService.findPlanById(plan.getPlanId());

            assertEquals(RepayPlanStatus.OVERDUE_SUCCEEDED.getStatus().intValue(), actualPlan.getStatus());
        }
        catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private LoanApplicationService loanApplicationService;

    @Autowired
    private UserService userService;

}