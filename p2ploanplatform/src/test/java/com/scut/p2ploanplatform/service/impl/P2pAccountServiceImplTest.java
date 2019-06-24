package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.BankAccountDao;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class P2pAccountServiceImplTest {
    @Autowired
    private P2pAccountServiceImpl p2pAccountService;

    @Autowired
    private BankAccountDao bankAccountDao;

    @Test
//    @Transactional
    public void insertP2pAccountTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal balance=new BigDecimal(1000);
        int result=p2pAccountService.insertP2pAccount("201736824347","Olivia","654321",balance,1,0);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void showBalanceTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal testBalance=p2pAccountService.showBalance("201736824347");
        BigDecimal balance=new BigDecimal(1000);
        assertEquals(0,testBalance.compareTo(balance));
    }

    @Test
    @Transactional
    public void verifyTradeTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal amount=new BigDecimal(500);
        Boolean result=p2pAccountService.verifyTrade("201736824347",amount);
        assertEquals(true,result);
    }

    @Test
    @Transactional
    public void transferTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal amount=new BigDecimal(500);
        Boolean result=p2pAccountService.transfer("201736824347","201636824347",amount);
        assertEquals(true,result);
        BigDecimal newPayerBalance=p2pAccountService.showBalance("201736824347");
        BigDecimal newPayeeBalance=p2pAccountService.showBalance("201636824347");
        assertEquals(0,new BigDecimal(500).compareTo(newPayerBalance));
        assertEquals(0,new BigDecimal(600).compareTo(newPayeeBalance));
    }

    @Test
    @Transactional
    public void rechargeTest() throws SQLException,IllegalArgumentException
    {
        Boolean result=p2pAccountService.recharge("201636824347","012345678901",new BigDecimal(50));
        assertEquals(true,result);
        BigDecimal newP2pBalance=p2pAccountService.showBalance("201636824347");
        BigDecimal newCardBalance=bankAccountDao.findBalanceByCardId("012345678901");
        assertEquals(0,new BigDecimal(50).compareTo(newP2pBalance));
        assertEquals(0,new BigDecimal(550).compareTo(newCardBalance));
    }

    @Test
    @Transactional
    public void withdrawTest() throws SQLException,IllegalArgumentException
    {
        Boolean result=p2pAccountService.withdraw("201636824347","012345678901",new BigDecimal(200));
        assertEquals(true,result);
        BigDecimal newP2pBalance=p2pAccountService.showBalance("201636824347");
        BigDecimal newCardBalance=bankAccountDao.findBalanceByCardId("012345678901");
        assertEquals(0,new BigDecimal(300).compareTo(newP2pBalance));
        assertEquals(0,new BigDecimal(300).compareTo(newCardBalance));
    }
}
