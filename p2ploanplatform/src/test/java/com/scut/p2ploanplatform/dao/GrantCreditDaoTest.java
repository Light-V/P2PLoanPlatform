package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.GrantCredit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
        grantCredit.setIncome(10000.0);
        grantCredit.setQuota(50000.0);
        grantCredit.setRate(1.01);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.setTime(new Date());
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
        assertEquals(grantCredit.toString(), actual.toString());
    }

    @Test
    @Transactional
    public void deleteGrantCredit() {
        grantCreditDao.insertGrantCredit(grantCredit);
        assertEquals(1, grantCreditDao.deleteGrantCredit(grantCredit.getUserId()));
    }
}