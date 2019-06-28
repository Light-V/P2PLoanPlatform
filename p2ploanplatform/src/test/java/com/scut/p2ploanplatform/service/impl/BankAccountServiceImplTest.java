package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.P2pAccountDao;
import com.scut.p2ploanplatform.entity.BankAccount;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BankAccountServiceImplTest {
    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @Autowired
    private P2pAccountDao p2pAccountDao;

    @Test
    @Transactional
    public void addBankAccountTest() throws SQLException,IllegalArgumentException
    {
        int result=bankAccountService.addBankAccount("123456789012","201636824347","123456",new BigDecimal(1000));
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void findCardByThirdPartyIdTest() throws SQLException,IllegalArgumentException
    {
        bankAccountService.addBankAccount("123456789012","201636824347","123456",new BigDecimal(1000));
        BankAccount bankAccount=bankAccountService.findCardByThirdPartyId("201636824347");
        assertNotNull(bankAccount.getCardID());
        assertNotNull(bankAccount.getThirdPartyId());
        assertNotNull(bankAccount.getBalance());
        assertNotNull(bankAccount.getPaymentPassword());
    }

    @Test
    @Transactional
    public void untieBankAccountTest() throws SQLException,IllegalArgumentException
    {
        bankAccountService.addBankAccount("123456789012","201636824347","123456",new BigDecimal(1000));
        bankAccountService.untieBankAccount("201636824347","123456789012");
        assertNull(bankAccountService.findCardByThirdPartyId("201636824347"));
    }

    @Test
    @Transactional
    public void verifyCardIsAdded() throws SQLException,IllegalArgumentException
    {
        bankAccountService.addBankAccount("123456789012","201636824347","123456",new BigDecimal(1000));
        Boolean trueResult=bankAccountService.verifyCardIsAdded("123456789012");
        Boolean falseResult=bankAccountService.verifyCardIsAdded("123456789011");
        assertEquals(true,trueResult);
        assertEquals(false,falseResult);
    }

    @Test
    @Transactional
    public void verifyPasswordTest() throws SQLException,IllegalArgumentException
    {
        bankAccountService.addBankAccount("123456789012","201636824347","123456",new BigDecimal(1000));
        String correctPassword="123456";
        String wrongPassword="123455";
        Boolean trueResult=bankAccountService.verifyPassword("123456789012",correctPassword);
        Boolean falseResult=bankAccountService.verifyPassword("123456789012",wrongPassword);
        assertEquals(true,trueResult);
        assertEquals(false,falseResult);
    }
}
