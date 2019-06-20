package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.CreditInfo;
import com.scut.p2ploanplatform.entity.GrantCredit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author: Light
 * @date: 2019/6/18 16:27
 * @description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class CreditServiceImplTest {

    @Autowired
    CreditServiceImpl creditService;

    private CreditInfo creditInfo;
    private GrantCredit grantCredit;

    @Before
    public void setUp() {
        creditInfo = new CreditInfo();
        creditInfo.setUserId("1234567890ab");
        creditInfo.setIncome(new BigDecimal("10000.00"));
        creditInfo.setDebt(new BigDecimal("0.00"));
        creditInfo.setFamilyIncome(new BigDecimal("50000.00"));
        creditInfo.setAssets(new BigDecimal("1000000.00"));
        creditInfo.setCreditScore(120);
        creditInfo.setFamilyNumber(10);

        grantCredit = new GrantCredit();
        grantCredit.setUserId(creditInfo.getUserId());
        grantCredit.setQuota(new BigDecimal("100000.00"));
        grantCredit.setRate(new BigDecimal("1.00"));
        grantCredit.setIncome(creditInfo.getIncome());
        grantCredit.setExpire(new Date());

    }
    @Test
    @Transactional
    public void creditReport() {
        creditService.updateCreditInfo(creditInfo);
        try {
            assertEquals(new BigDecimal("2.4"), creditService.creditReport(creditInfo.getUserId()));
        }
        catch (Exception exception) {

        }
    }

    @Test
    @Transactional
    public void getCreditInfo() {
        creditService.updateCreditInfo(creditInfo);
        try {
            CreditInfo actual = creditService.getCreditInfo(creditInfo.getUserId());
            assertEquals(creditInfo, actual);
        }
        catch (Exception exception) {

        }
    }

    @Test
    @Transactional
    public void creditGrant() {
        try {
            creditService.updateCreditInfo(creditInfo);
            BigDecimal result = creditService.creditReport(creditInfo.getUserId());
            assertEquals(new BigDecimal(2.4), result);
            creditService.creditGrant(creditInfo.getUserId(), new BigDecimal(2.0));
        }
        catch (Exception exception) {

        }
    }
}