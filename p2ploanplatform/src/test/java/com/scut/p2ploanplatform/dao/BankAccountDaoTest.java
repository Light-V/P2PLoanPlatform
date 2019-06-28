package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.BankAccount;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BankAccountDaoTest {
    @Autowired
    private BankAccountDao bankAccountDao;

    private BankAccount sampleBankAccount=new BankAccount();

    private BigDecimal balance=new BigDecimal(500);

    @Before
    public void setUp() throws Exception
    {
        sampleBankAccount.setCardID("012345678901");
        sampleBankAccount.setThirdPartyId("201636824347");
        sampleBankAccount.setPaymentPassword("123456");
        sampleBankAccount.setBalance(balance);
    }

    @Test
    @Transactional
    public void addBankAccountTest()
    {
        int result=bankAccountDao.addBankAccount(sampleBankAccount);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void findCardByCardIdTest()
    {
        bankAccountDao.addBankAccount(sampleBankAccount);
        BankAccount bankAccount=bankAccountDao.findCardByCardId(sampleBankAccount.getCardID());
        assertNotNull(bankAccount.getCardID());
        assertNotNull(bankAccount.getThirdPartyId());
        assertNotNull(bankAccount.getBalance());
        assertNotNull(bankAccount.getPaymentPassword());
    }

    @Test
    @Transactional
    public void findBalanceByCardIdTest()
    {
        bankAccountDao.addBankAccount(sampleBankAccount);
        BigDecimal testBalance=bankAccountDao.findBalanceByCardId(sampleBankAccount.getCardID());
        assertEquals(0,balance.compareTo(testBalance));
    }

    @Test
    @Transactional
    public void findPaymentPasswordByCardIdTest()
    {
        bankAccountDao.addBankAccount(sampleBankAccount);
        String password=bankAccountDao.findPaymentPasswordByCardId(sampleBankAccount.getCardID());
        assertEquals(password, sampleBankAccount.getPaymentPassword());
    }

    @Test
    @Transactional
    public void findCardByThirdPartyIdTest()
    {
        bankAccountDao.addBankAccount(sampleBankAccount);
        BankAccount bankAccount=bankAccountDao.findCardByThirdPartyId(sampleBankAccount.getThirdPartyId());
        assertNotNull(bankAccount.getCardID());
        assertNotNull(bankAccount.getThirdPartyId());
        assertNotNull(bankAccount.getBalance());
        assertNotNull(bankAccount.getPaymentPassword());
    }

    @Test
    @Transactional
    public void updateBalanceTest()
    {
        bankAccountDao.addBankAccount(sampleBankAccount);
        BigDecimal newBalance=new BigDecimal(300);
        int result=bankAccountDao.updateBalance(sampleBankAccount.getCardID(),newBalance);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void deleteCardTest()
    {
        bankAccountDao.addBankAccount(sampleBankAccount);
        assertNotNull(sampleBankAccount.getCardID());
        assertNotNull(sampleBankAccount.getThirdPartyId());
        assertNotNull(sampleBankAccount.getBalance());
        assertNotNull(sampleBankAccount.getPaymentPassword());
        bankAccountDao.deleteCard(sampleBankAccount.getCardID());
        assertNull(bankAccountDao.findCardByCardId(sampleBankAccount.getCardID()));
    }
}
