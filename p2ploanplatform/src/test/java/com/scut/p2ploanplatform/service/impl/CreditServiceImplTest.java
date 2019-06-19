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
        creditInfo.setIncome(10000.);
        creditInfo.setDebt(0.);
        creditInfo.setFamilyIncome(50000.);
        creditInfo.setAssets(1000000.);
        creditInfo.setCreditScore(120);
        creditInfo.setFamilyNumber(10);

        grantCredit = new GrantCredit();
        grantCredit.setUserId(creditInfo.getUserId());
        grantCredit.setQuota(100000.);
        grantCredit.setRate(1.);
        grantCredit.setIncome(creditInfo.getIncome());
        grantCredit.setExpire(new Date());

    }
    @Test
    //@Transactional
    public void creditReport() {
        //TODO
    }

    @Test
    public void getCreditInfo() {
        //TODO
    }

    @Test
    @Transactional
    public void creditGrant() {
        try {
            creditService.updateCreditInfo(creditInfo);
            Double result = creditService.creditReport(creditInfo.getUserId());
            assertEquals(new Double(2.4), result);
            creditService.creditGrant(creditInfo.getUserId(), 2.0);
        }
        catch (Exception exception) {

        }
    }
}