package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.P2pAccount;
import com.scut.p2ploanplatform.enums.AccountTypeEnum;
import com.scut.p2ploanplatform.enums.P2pAccountStatusEnum;
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
public class P2pAccountDaoTest {
    @Autowired
    private P2pAccountDao p2pAccountDao;

    private P2pAccount sampleP2pAccount=new P2pAccount();

    private BigDecimal balance=new BigDecimal(100);

    @Before
    public void setUp() throws Exception
    {
        sampleP2pAccount.setUserId("201636824347");
        sampleP2pAccount.setName("Oliver");
        sampleP2pAccount.setPaymentPassword("123456");
        sampleP2pAccount.setBalance(balance);
        sampleP2pAccount.setStatus(P2pAccountStatusEnum.ACTIVE.getCode());
        sampleP2pAccount.setType(AccountTypeEnum.NORMAL.getCode());
    }

    @Test
//    @Transactional
    public void insertP2pAccountTest()
    {
        int result=p2pAccountDao.insertP2pAccount(sampleP2pAccount);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void findBalanceByUserIdTest()
    {
        BigDecimal testBalance=p2pAccountDao.findBalanceByUserId(sampleP2pAccount.getUserId());
        assertEquals(0,balance.compareTo(testBalance));
    }

    @Test
    @Transactional
    public void findByUserIdTest()
    {
        P2pAccount p2pAccountTest=p2pAccountDao.findByUserId(sampleP2pAccount.getUserId());
        assertNotNull(p2pAccountTest.getUserId());
        assertNotNull(p2pAccountTest.getBalance());
        assertNotNull(p2pAccountTest.getName());
        assertNotNull(p2pAccountTest.getStatus());
        assertNotNull(p2pAccountTest.getType());
        assertNotNull(p2pAccountTest.getPaymentPassword());
    }

    @Test
    @Transactional
    public void findByStatusTest()
    {
        P2pAccount newP2pAccount=new P2pAccount();
        newP2pAccount.setUserId("201736824347");
        newP2pAccount.setName("Oliver");
        newP2pAccount.setPaymentPassword("123456");
        newP2pAccount.setBalance(balance);
        newP2pAccount.setStatus(P2pAccountStatusEnum.ACTIVE.getCode());
        newP2pAccount.setType(AccountTypeEnum.NORMAL.getCode());
        p2pAccountDao.insertP2pAccount(newP2pAccount);
        List<P2pAccount> p2pAccountList=p2pAccountDao.findByStatus(P2pAccountStatusEnum.ACTIVE.getCode());
        for (P2pAccount account: p2pAccountList)
        {
            assertNotNull(account.getUserId());
            assertNotNull(account.getName());
            assertNotNull(account.getPaymentPassword());
            assertNotNull(account.getBalance());
            assertNotNull(account.getType());
            assertNotNull(account.getStatus());
        }
    }

    @Test
    @Transactional
    public void updateBalanceTest()
    {
        BigDecimal newBalance=new BigDecimal(200);
        int result=p2pAccountDao.updateBalance(sampleP2pAccount.getUserId(),newBalance);
        assertEquals(1,result);
        assertEquals(0,newBalance.compareTo(p2pAccountDao.findBalanceByUserId(sampleP2pAccount.getUserId())));
    }

    @Test
    @Transactional
    public void updateStatusTest()
    {
        Integer newStatus=P2pAccountStatusEnum.FROZEN.getCode();
        int result=p2pAccountDao.updateStatus(sampleP2pAccount.getUserId(), newStatus);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void updateNameTest()
    {
        String newName="Olivia";
        int result=p2pAccountDao.updateName(sampleP2pAccount.getUserId(),newName);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void updatePaymentPasswordTest()
    {
        String newPaymentPassword="654321";
        int result=p2pAccountDao.updatePaymentPassword(sampleP2pAccount.getUserId(),newPaymentPassword);
        assertEquals(1,result);
    }
}
