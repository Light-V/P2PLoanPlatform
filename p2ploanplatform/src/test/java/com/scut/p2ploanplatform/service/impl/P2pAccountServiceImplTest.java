package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.BankAccountDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class P2pAccountServiceImplTest {
    @Autowired
    private P2pAccountServiceImpl p2pAccountService;

    @Autowired
    private BankAccountDao bankAccountDao;

    @Autowired
    private BankAccountServiceImpl bankAccountService;

    @Test
    @Transactional
    public void addP2pAccountTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        int result1=p2pAccountService.addP2pAccount("201636824347","123456",balance,1,0);
        int result2=p2pAccountService.addP2pAccount("201736824347","654321",balance,1,0);
        assertEquals(1,result1);
        assertEquals(1,result2);
    }

    @Test
    @Transactional
    public void findBalanceTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        p2pAccountService.addP2pAccount("201736824347","654321",balance,1,0);
        BigDecimal testBalance=p2pAccountService.findBalance("201736824347");
        assertEquals(0,testBalance.compareTo(balance));
    }

    @Test
    @Transactional
    public void verifyTradeTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        p2pAccountService.addP2pAccount("201636824347","123456",balance,1,0);
        p2pAccountService.addP2pAccount("201736824347","654321",balance,1,0);
        BigDecimal amount1=new BigDecimal(1000);
        BigDecimal amount2=new BigDecimal(999);
        BigDecimal amount3=new BigDecimal(1001);
        Boolean result1=p2pAccountService.verifyTrade("201736824347",amount1);
        Boolean result2=p2pAccountService.verifyTrade("201736824347",amount2);
        Boolean result3=p2pAccountService.verifyTrade("201736824347",amount3);
        assertEquals(true,result1);
        assertEquals(true,result2);
        assertEquals(false,result3);
    }

    @Test
    @Transactional
    public void verifyPasswordTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        int result1=p2pAccountService.addP2pAccount("201636824347","123456",balance,1,0);
        String correctPassword="123456";
        String wrongPassword="123455";
        Boolean trueResult=p2pAccountService.verifyPassword("201636824347",correctPassword);
        Boolean falseResult=p2pAccountService.verifyPassword("201636824347",wrongPassword);
        assertEquals(true,trueResult);
        assertEquals(false,falseResult);
    }

    @Test
    @Transactional
    public void transferTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        p2pAccountService.addP2pAccount("201636824347","123456",balance,1,0);
        p2pAccountService.addP2pAccount("201736824347","654321",balance,1,0);
        BigDecimal amount=new BigDecimal(200);
        Boolean result=p2pAccountService.transfer("201736824347","201636824347",amount);
        assertEquals(true,result);
        BigDecimal newPayerBalance=p2pAccountService.findBalance("201736824347");
        BigDecimal newPayeeBalance=p2pAccountService.findBalance("201636824347");
        assertEquals(0,new BigDecimal(800).compareTo(newPayerBalance));
        assertEquals(0,new BigDecimal(1200).compareTo(newPayeeBalance));
    }

    @Test
    @Transactional
    public void verifyPasswordIsSetTest() throws SQLException,IllegalArgumentException
    {
        p2pAccountService.addP2pAccount("201636824347","",new BigDecimal(1000),1,0);
        p2pAccountService.addP2pAccount("201736824347","654321",new BigDecimal(1000),1,0);
        Boolean result1=p2pAccountService.verifyPasswordIsSet("201636824347");
        Boolean result2=p2pAccountService.verifyPasswordIsSet("201736824347");
        assertEquals(false,result1);
        assertEquals(true,result2);
    }

    @Test
    @Transactional
    public void rechargeTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        p2pAccountService.addP2pAccount("201636824347","123456",balance,1,0);
        bankAccountService.addBankAccount("012345678901","201636824347","123456",balance);
        Boolean result=p2pAccountService.recharge("201636824347","012345678901",new BigDecimal(200));
        assertEquals(true,result);
        BigDecimal newP2pBalance=p2pAccountService.findBalance("201636824347");
        BigDecimal newCardBalance=bankAccountDao.findBalanceByCardId("012345678901");
        assertEquals(0,new BigDecimal(1200).compareTo(newP2pBalance));
        assertEquals(0,new BigDecimal(800).compareTo(newCardBalance));
    }

    @Test
    @Transactional
    public void withdrawTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        p2pAccountService.addP2pAccount("201636824347","123456",balance,1,0);
        bankAccountService.addBankAccount("012345678901","201636824347","123456",balance);
        Boolean result=p2pAccountService.withdraw("201636824347","012345678901",new BigDecimal(200));
        assertEquals(true,result);
        BigDecimal newP2pBalance=p2pAccountService.findBalance("201636824347");
        BigDecimal newCardBalance=bankAccountDao.findBalanceByCardId("012345678901");
        assertEquals(0,new BigDecimal(800).compareTo(newP2pBalance));
        assertEquals(0,new BigDecimal(1200).compareTo(newCardBalance));
    }


}
