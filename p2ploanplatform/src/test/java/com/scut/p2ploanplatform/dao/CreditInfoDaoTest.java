package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.CreditInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * @author: Light
 * @date: 2019/6/17 20:37
 * @description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditInfoDaoTest {

    @Autowired
    private CreditInfoDao creditInfoDao;

    private CreditInfo creditInfo;

    @Before
    public void setUp() {
        creditInfo = new CreditInfo();
        creditInfo.setUserId("2ck9f9d8rju3");
        creditInfo.setIncome(new BigDecimal("10000.00"));
        creditInfo.setFamilyNumber(10);
        creditInfo.setAssets(new BigDecimal("1000000.00"));
        creditInfo.setFamilyIncome(new BigDecimal("50000.00"));
        creditInfo.setDebt(new BigDecimal("0.00"));
        creditInfo.setCreditScore(100);
    }

    @Test
    @Transactional
    public void insertCreditInfo() {
        assertEquals(1, creditInfoDao.insertCreditInfo(creditInfo));
    }


    @Test
    @Transactional
    public void updateCreditInfo()
    {
        /*creditInfoDao.insertCreditInfo(creditInfo);
        creditInfo.setIncome(new BigDecimal("500.00"));
        assertEquals(1, creditInfoDao.updateCreditInfo(creditInfo));*/
    }


    @Test
    @Transactional
    public void selectCreditInfo()
    {
        creditInfoDao.insertCreditInfo(creditInfo);
        CreditInfo actual = creditInfoDao.selectCreditInfo(creditInfo.getUserId());
        isCreditInfoEqual(creditInfo, actual);
    }

    @Test
    @Transactional
    public void deleteCreditInfo()
    {
        creditInfoDao.insertCreditInfo(creditInfo);
        assertEquals(1, creditInfoDao.deleteCreditInfo(creditInfo.getUserId()));
    }

    public void isCreditInfoEqual(CreditInfo expect, CreditInfo actual) throws AssertionError {
        assertEquals(expect.getAssets(), actual.getAssets());
        assertEquals(expect.getCreditScore(), actual.getCreditScore());
        assertEquals(expect.getDebt(), actual.getDebt());
        assertEquals(expect.getFamilyIncome(), actual.getFamilyIncome());
        assertEquals(expect.getFamilyNumber(), actual.getFamilyNumber());
        assertEquals(expect.getIncome(), actual.getIncome());
        assertEquals(expect.getUserId(), actual.getUserId());
    }
}