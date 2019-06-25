package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.P2pAccountDao;
import com.scut.p2ploanplatform.entity.BankAccount;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.JVM)
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
    public void showBalanceByCardIdTest() throws SQLException,IllegalArgumentException
    {
        BigDecimal testBalance=bankAccountService.findBalanceByCardId("012345678901");
        assertEquals(0,new BigDecimal(500).compareTo(testBalance));
    }



}
