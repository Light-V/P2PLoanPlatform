package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.*;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RepayPlanDaoTest {

    @Autowired
    private RepayPlanDao repayPlanDao;

    private RepayPlan sampleRepayPlan;

    @Before
    public void setUp() throws Exception {
        sampleRepayPlan = new RepayPlan();
        sampleRepayPlan.setPlanId("b971ec79e7484ddb835c4da691cc82c3");
        sampleRepayPlan.setAmount(BigDecimal.valueOf(1123.45));
        sampleRepayPlan.setPurchaseId(1234552211);
        Date repayDate = offsetOneMonth(getDate(new Date()), 1);
        sampleRepayPlan.setRepayDate(repayDate);
        sampleRepayPlan.setRealRepayDate(repayDate);
    }

    @Test
    @Transactional
    public void insertPlanTest() {
        int result = insertPlan(sampleRepayPlan);
        assertEquals(1, result);
    }

    private int insertPlan(RepayPlan plan) {
        return repayPlanDao.insertPlan(plan);
    }

    @Test
    @Transactional
    public void findPlanByPurchaseIdTest() {
        // checking empty list
        List<RepayPlan> repayPlans = repayPlanDao.findPlanByPurchaseId(1234552211);
        assertEquals(0, repayPlans.size());

        // checking non-empty list
        insertPlan(sampleRepayPlan);
        repayPlans = repayPlanDao.findPlanByPurchaseId(1234552211);
        assertEquals(1, repayPlans.size());
        RepayPlan repayPlan = repayPlans.get(0);
        assertRepayPlanEquals(sampleRepayPlan, repayPlan);
    }

    private void assertRepayPlanEquals(RepayPlan expected, RepayPlan actual) throws AssertionError {
        assertEquals(expected.getPlanId(), actual.getPlanId());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getPurchaseId(), actual.getPurchaseId());
        assertEquals(expected.getRepayDate(), actual.getRepayDate());
        assertEquals(expected.getRealRepayDate(), actual.getRealRepayDate());
        assertEquals(expected.getStatus(), actual.getStatus());
    }

    private Date getDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.setTime(date);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date offsetOneMonth(Date date, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.setTime(date);
        c.add(Calendar.MONTH, amount);
        return c.getTime();
    }

    @Test
    @Transactional
    public void findPlanByIdTest() {
        // check: empty result
        RepayPlan plan = repayPlanDao.findPlanByPlanId(UUID.randomUUID().toString().replace("-", ""));
        assertNull(plan);

        // check: non-empty result
        insertPlan(sampleRepayPlan);
        plan = repayPlanDao.findPlanByPlanId(sampleRepayPlan.getPlanId());
        assertRepayPlanEquals(sampleRepayPlan, plan);
    }

    @Test
    @Transactional
    public void findAllUnpaidPlanTest() {
        // insert something into database

        // unpaid plans (will be paid today)
        RepayPlan unpaidPlan = new RepayPlan();
        unpaidPlan.setPlanId(UUID.randomUUID().toString().replace("-",""));
        unpaidPlan.setPurchaseId(1234);
        unpaidPlan.setRepayDate(getDate(new Date()));
        unpaidPlan.setRealRepayDate(null);
        unpaidPlan.setAmount(BigDecimal.valueOf(666.66));
        unpaidPlan.setStatus(RepayPlanStatus.SCHEDULED.getStatus());

        // overdue plans
        RepayPlan overduePlan = new RepayPlan();
        overduePlan.setPlanId(UUID.randomUUID().toString().replace("-",""));
        overduePlan.setPurchaseId(1234);
        overduePlan.setRepayDate(offsetOneMonth(getDate(new Date()), -1));
        overduePlan.setRealRepayDate(null);
        overduePlan.setAmount(BigDecimal.valueOf(123, 2));
        overduePlan.setStatus(RepayPlanStatus.OVERDUE.getStatus());

        // normal plans
        RepayPlan normalPlan = new RepayPlan();
        normalPlan.setPlanId(UUID.randomUUID().toString().replace("-",""));
        normalPlan.setPurchaseId(1234);
        normalPlan.setRepayDate(offsetOneMonth(getDate(new Date()), -2));
        normalPlan.setRealRepayDate(normalPlan.getRepayDate());
        normalPlan.setAmount(BigDecimal.valueOf(321, 2));
        normalPlan.setStatus(RepayPlanStatus.SUCCEEDED.getStatus());

        // normal plans
        RepayPlan advancedPaidPlan = new RepayPlan();
        advancedPaidPlan.setPlanId(UUID.randomUUID().toString().replace("-",""));
        advancedPaidPlan.setPurchaseId(1234);
        advancedPaidPlan.setRepayDate(offsetOneMonth(getDate(new Date()), -2));
        advancedPaidPlan.setRealRepayDate(advancedPaidPlan.getRepayDate());
        advancedPaidPlan.setAmount(BigDecimal.valueOf(321, 2));
        advancedPaidPlan.setStatus(RepayPlanStatus.GUARANTOR_PAID_ADVANCE.getStatus());

        insertPlan(unpaidPlan);
        insertPlan(overduePlan);
        insertPlan(normalPlan);
        insertPlan(advancedPaidPlan);

        List<RepayPlan> plans = repayPlanDao.findAllUnpaidPlan();

        assertEquals(3, plans.size());

        if (plans.get(0).getPlanId().equals(unpaidPlan.getPlanId())) {
            assertRepayPlanEquals(unpaidPlan, plans.get(0));
            assertRepayPlanEquals(overduePlan, plans.get(1));
        } else {
            assertRepayPlanEquals(overduePlan, plans.get(0));
            assertRepayPlanEquals(unpaidPlan, plans.get(1));
        }
    }

    @Test
    @Transactional
    public void updatePlanTest() {
        // check: non-exist plan
        int result = repayPlanDao.updatePlan(sampleRepayPlan);
        assertEquals(0, result);

        insertPlan(sampleRepayPlan);
        RepayPlan actualPlan = repayPlanDao.findPlanByPlanId(sampleRepayPlan.getPlanId());
        assertRepayPlanEquals(sampleRepayPlan, actualPlan);

        actualPlan.setStatus(RepayPlanStatus.SUCCEEDED.getStatus());
        actualPlan.setRepayDate(offsetOneMonth(actualPlan.getRepayDate(), 1));
        actualPlan.setRealRepayDate(actualPlan.getRepayDate());
        actualPlan.setAmount(BigDecimal.valueOf(2333.45));

        result = repayPlanDao.updatePlan(actualPlan);
        assertEquals(1, result);
        RepayPlan updatedPlan = repayPlanDao.findPlanByPlanId(sampleRepayPlan.getPlanId());
        assertRepayPlanEquals(updatedPlan, actualPlan);
    }

    @Test
    @Transactional
    public void deletePlanTest() {
        int result = repayPlanDao.deletePlan(sampleRepayPlan.getPlanId());
        assertEquals(0, result);

        insertPlan(sampleRepayPlan);
        result = repayPlanDao.deletePlan(sampleRepayPlan.getPlanId());
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void updatePlanStatusTest() {

        // unpaid plans (will be paid today)
        RepayPlan unpaidPlan = new RepayPlan();
        unpaidPlan.setPlanId(UUID.randomUUID().toString().replace("-",""));
        unpaidPlan.setPurchaseId(1234);
        unpaidPlan.setRepayDate(getDate(new Date()));
        unpaidPlan.setRealRepayDate(null);
        unpaidPlan.setAmount(BigDecimal.valueOf(666.66));
        unpaidPlan.setStatus(RepayPlanStatus.SCHEDULED.getStatus());

        // overdue plans
        RepayPlan overduePlan = new RepayPlan();
        overduePlan.setPlanId(UUID.randomUUID().toString().replace("-",""));
        overduePlan.setPurchaseId(1234);
        overduePlan.setRepayDate(offsetOneMonth(getDate(new Date()), -1));
        overduePlan.setRealRepayDate(null);
        overduePlan.setAmount(BigDecimal.valueOf(123, 2));
        overduePlan.setStatus(RepayPlanStatus.OVERDUE.getStatus());

        // overdue plans (with wrong status)
        RepayPlan overduePlan2 = new RepayPlan();
        overduePlan2.setPlanId(UUID.randomUUID().toString().replace("-",""));
        overduePlan2.setPurchaseId(1234);
        overduePlan2.setRepayDate(offsetOneMonth(getDate(new Date()), -1));
        overduePlan2.setRealRepayDate(null);
        overduePlan2.setAmount(BigDecimal.valueOf(123, 2));
        overduePlan2.setStatus(RepayPlanStatus.SCHEDULED.getStatus());

        // normal plans
        RepayPlan normalPlan = new RepayPlan();
        normalPlan.setPlanId(UUID.randomUUID().toString().replace("-",""));
        normalPlan.setPurchaseId(1234);
        normalPlan.setRepayDate(offsetOneMonth(getDate(new Date()), -2));
        normalPlan.setRealRepayDate(normalPlan.getRepayDate());
        normalPlan.setAmount(BigDecimal.valueOf(321, 2));
        normalPlan.setStatus(RepayPlanStatus.SUCCEEDED.getStatus());

        insertPlan(unpaidPlan);
        insertPlan(overduePlan);
        insertPlan(overduePlan2);
        insertPlan(normalPlan);

        int result = repayPlanDao.updatePlanStatus();
        assertEquals(1, result);
    }
}