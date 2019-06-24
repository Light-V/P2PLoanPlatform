package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.BankAccount;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.JVM)
public class BankAccountDaoTest {
    @Autowired
    private BankAccountDao bankAccountDao;

    private BankAccount sampleBankAccount=new BankAccount();

    private BigDecimal balance=new BigDecimal(500);

    @Before
    public void setUp() throws Exception
    {
        sampleBankAccount.setCardID("012345678901");
        sampleBankAccount.setUserID("201636824347");
        sampleBankAccount.setName("Oliver");
        sampleBankAccount.setPaymentPassword("123456");
        sampleBankAccount.setBalance(balance);
    }

    @Test
//    @Transactional
    public void insertBankAccountTest()
    {
        int result=bankAccountDao.insertBankAccount(sampleBankAccount);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void findBalanceByCardIdTest()
    {
        BigDecimal testBalance=bankAccountDao.findBalanceByCardId(sampleBankAccount.getCardID());
        assertEquals(0,balance.compareTo(testBalance));
    }

    @Test
    @Transactional
    public void findCardsByUserIdTest()
    {
        BankAccount newBankAccount=new BankAccount();
        newBankAccount.setCardID("111111111111");
        newBankAccount.setUserID("201636824347");
        newBankAccount.setName("Oliver");
        newBankAccount.setPaymentPassword("123456");
        newBankAccount.setBalance(balance);
        bankAccountDao.insertBankAccount(newBankAccount);
        List<BankAccount> bankAccountList=bankAccountDao.findCardsByUserId(sampleBankAccount.getUserID());
        for (BankAccount bankAccount:bankAccountList)
        {
            assertNotNull(bankAccount.getCardID());
            assertNotNull(bankAccount.getUserID());
            assertNotNull(bankAccount.getBalance());
            assertNotNull(bankAccount.getPaymentPassword());
            assertNotNull(bankAccount.getName());
        }
    }

    @Test
    @Transactional
    public void updateBalanceTest()
    {
        BigDecimal newBalance=new BigDecimal(300);
        int result=bankAccountDao.updateBalance(sampleBankAccount.getCardID(),newBalance);
        assertEquals(1,result);
    }
}
