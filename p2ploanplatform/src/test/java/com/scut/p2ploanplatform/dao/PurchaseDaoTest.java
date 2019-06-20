package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.enums.LoanStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseDaoTest {

    @Autowired
    private PurchaseDao purchaseDao;

    private Purchase purchase = new Purchase();

    @Before
    public void newApplication(){
        purchase.setApplicationId(8);
        purchase.setBorrowerId("201630664195");
        purchase.setGuarantorId("201630219652");
        purchase.setInvestorId("206613679426");
        purchase.setTitle("撒娇打滚求借钱");
        purchase.setPurchaseTime(Calendar.getInstance().getTime());
        purchase.setStatus(LoanStatus.SUBSCRIBED.getStatus());
        purchase.setAmount(new BigDecimal(1000000));
        purchase.setInterestRate(new BigDecimal(0.0618));
        purchase.setLoanMonth(3);
    }

    @Test
    @Transactional
    public void createPurchaseItem() {
        Boolean result = purchaseDao.createPurchaseItem(purchase);
        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void purchaseUpdate() {
        purchaseDao.createPurchaseItem(purchase);
        purchase.setStatus(LoanStatus.OVERDUE.getStatus());
        Boolean result = purchaseDao.updatePurchase(purchase);
        Assert.assertTrue(result);
        Assert.assertEquals(new Integer(5), purchase.getStatus());
    }

    @Test
    @Transactional
    public void showAllPurchase() {
        for(int i = 0; i<5; i++){
            purchaseDao.createPurchaseItem(purchase);
        }
        List<Purchase> purchases = purchaseDao.showAllPurchase();
        Assert.assertEquals(5,purchases.size());
    }

    @Test
    @Transactional
    public void showPurchaseByPurchaseId() {
        for(int i = 0; i<5; i++){
            purchaseDao.createPurchaseItem(purchase);
        }
        Purchase purchaseitem = purchaseDao.showPurchaseByPurchaseId(10);
    }

    @Test
    @Transactional
    public void showPurchaseByInvestorId() {
        for(int i = 0; i<5; i++){
            purchaseDao.createPurchaseItem(purchase);
        }
        List<Purchase> purchases = purchaseDao.showPurchaseByInvestorId("206613679426");
        Assert.assertEquals(5,purchases.size());
    }

    @Test
    @Transactional
    public void showPurchaseByBorrowerId() {
        for(int i = 0; i<5; i++){
            purchaseDao.createPurchaseItem(purchase);
        }
        List<Purchase> purchases = purchaseDao.showPurchaseByBorrowerId("206613679426");
        Assert.assertEquals(0,purchases.size());
    }
}