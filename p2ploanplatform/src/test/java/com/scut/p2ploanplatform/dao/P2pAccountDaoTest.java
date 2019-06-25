package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.P2pAccount;
import com.scut.p2ploanplatform.enums.AccountTypeEnum;
import com.scut.p2ploanplatform.enums.P2pAccountStatusEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class P2pAccountDaoTest {
    @Autowired
    private P2pAccountDao p2pAccountDao;

    private P2pAccount sampleP2pAccount=new P2pAccount();

    private BigDecimal balance=new BigDecimal(100);

    @Before
    public void setUp() throws Exception
    {
        sampleP2pAccount.setThirdPartyId("201636824347");
        sampleP2pAccount.setPaymentPassword("123456");
        sampleP2pAccount.setBalance(balance);
        sampleP2pAccount.setStatus(P2pAccountStatusEnum.ACTIVE.getCode());
        sampleP2pAccount.setType(AccountTypeEnum.NORMAL.getCode());
    }

    @Test
    @Transactional
    public void addP2pAccountTest()
    {
        int result=p2pAccountDao.addP2pAccount(sampleP2pAccount);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void findBalanceByThirdPartyIdTest()
    {
        p2pAccountDao.addP2pAccount(sampleP2pAccount);
        BigDecimal testBalance=p2pAccountDao.findBalanceByThirdPartyId(sampleP2pAccount.getThirdPartyId());
        assertEquals(0,balance.compareTo(testBalance));
    }

    @Test
    @Transactional
    public void findByThirdPartyIdTest()
    {
        p2pAccountDao.addP2pAccount(sampleP2pAccount);
        P2pAccount p2pAccountTest=p2pAccountDao.findByThirdPartyId(sampleP2pAccount.getThirdPartyId());
        assertNotNull(p2pAccountTest.getThirdPartyId());
        assertNotNull(p2pAccountTest.getBalance());
        assertNotNull(p2pAccountTest.getStatus());
        assertNotNull(p2pAccountTest.getType());
        assertNotNull(p2pAccountTest.getPaymentPassword());
    }

    @Test
    @Transactional
    public void findPasswordByThirdPartyIdTest()
    {
        p2pAccountDao.addP2pAccount(sampleP2pAccount);
        String password=p2pAccountDao.findPasswordByThirdPartyId(sampleP2pAccount.getThirdPartyId());
        assertEquals(password,sampleP2pAccount.getPaymentPassword());
    }

    @Test
    @Transactional
    public void findByStatusTest()
    {
        p2pAccountDao.addP2pAccount(sampleP2pAccount);
        P2pAccount newP2pAccount=new P2pAccount();
        newP2pAccount.setThirdPartyId("201736824347");
        newP2pAccount.setPaymentPassword("123456");
        newP2pAccount.setBalance(balance);
        newP2pAccount.setStatus(P2pAccountStatusEnum.ACTIVE.getCode());
        newP2pAccount.setType(AccountTypeEnum.NORMAL.getCode());
        p2pAccountDao.addP2pAccount(newP2pAccount);
        List<P2pAccount> p2pAccountList=p2pAccountDao.findByStatus(P2pAccountStatusEnum.ACTIVE.getCode());
        for (P2pAccount account: p2pAccountList)
        {
            assertNotNull(account.getThirdPartyId());
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
        p2pAccountDao.addP2pAccount(sampleP2pAccount);
        BigDecimal newBalance=new BigDecimal(200);
        int result=p2pAccountDao.updateBalance(sampleP2pAccount.getThirdPartyId(),newBalance);
        assertEquals(1,result);
        assertEquals(0,newBalance.compareTo(p2pAccountDao.findBalanceByThirdPartyId(sampleP2pAccount.getThirdPartyId())));
    }

    @Test
    @Transactional
    public void updateStatusTest()
    {
        p2pAccountDao.addP2pAccount(sampleP2pAccount);
        Integer newStatus=P2pAccountStatusEnum.FROZEN.getCode();
        int result=p2pAccountDao.updateStatus(sampleP2pAccount.getThirdPartyId(), newStatus);
        assertEquals(1,result);
    }

    @Test
    @Transactional
    public void updatePaymentPasswordTest()
    {
        p2pAccountDao.addP2pAccount(sampleP2pAccount);
        String newPaymentPassword="654321";
        int result=p2pAccountDao.updatePaymentPassword(sampleP2pAccount.getThirdPartyId(),newPaymentPassword);
        assertEquals(1,result);
    }

}
