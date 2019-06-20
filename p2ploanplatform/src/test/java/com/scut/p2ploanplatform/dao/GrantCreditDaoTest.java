package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.GrantCredit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * @author: Light
 * @date: 2019/6/18 10:11
 * @description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GrantCreditDaoTest {

    @Autowired
    private GrantCreditDao grantCreditDao;

    private GrantCredit grantCredit = new GrantCredit();
    @Before
    public void setUp()
    {
        grantCredit.setUserId("9f94ifkso38c");
        grantCredit.setIncome(new BigDecimal("10000.00"));
        grantCredit.setQuota(new BigDecimal("50000.00"));
        grantCredit.setRate(new BigDecimal("1.01"));
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTime(new Date());
        cal.set(Calendar.MILLISECOND, 0);
        grantCredit.setExpire(cal.getTime());
    }

    @Test
    @Transactional
    public void insertGrantCredit() {
        assertEquals(1, grantCreditDao.insertGrantCredit(grantCredit));
    }

    @Test
    @Transactional
    public void updateGrantCredit() {
        grantCreditDao.insertGrantCredit(grantCredit);
        Calendar calendar = Calendar.getInstance();
        grantCredit.setExpire(calendar.getTime());
        assertEquals(1, grantCreditDao.updateGrantCredit(grantCredit));
    }

    @Test
    @Transactional
    public void selectGrantCredit() {
        grantCreditDao.insertGrantCredit(grantCredit);
        GrantCredit actual  = grantCreditDao.selectGrantCredit(grantCredit.getUserId());
        isCreditGrantEqual(grantCredit, actual);
    }

    @Test
    @Transactional
    public void deleteGrantCredit() {
        grantCreditDao.insertGrantCredit(grantCredit);
        assertEquals(1, grantCreditDao.deleteGrantCredit(grantCredit.getUserId()));
    }

    public void isCreditGrantEqual(GrantCredit expect, GrantCredit actual) throws AssertionError {
        assertEquals(expect.getExpire(), actual.getExpire());
        assertEquals(expect.getRate(), actual.getRate());
        assertEquals(expect.getQuota(), actual.getQuota());
        assertEquals(expect.getIncome(), actual.getIncome());
        assertEquals(expect.getUserId(), actual.getUserId());
    }
}