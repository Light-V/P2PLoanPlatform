package com.scut.p2ploanplatform.service.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.exception.LoanStatusException;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.service.RepayService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class PurchaseServiceImplTest {
    @Autowired
    PurchaseServiceImpl purchaseService;

    @Autowired
    RepayService repayService;

    @Autowired
    LoanApplicationService applicationService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private LoanApplication application = new LoanApplication();
    private String investorId = "201603000000";

    @Before
    public void newApplication() throws Exception{
        application.setBorrowerId("201601000000");
        application.setTitle("测试");
        application.setStatus(LoanStatus.UNREVIEWED.getStatus());
        application.setAmount(new BigDecimal(1000000));
        application.setInterestRate(new BigDecimal(0.0618));
        application.setLoanMonth(6);
        application.setPurchaseDeadline(Calendar.getInstance().getTime());
        applicationService.reviewPass(application.getApplicationId(), "201602000000");
    }

    @Test
//    @Transactional
    public void subscribed_success() {
        Integer applicationId = application.getApplicationId();
        Purchase purchase=null;
        LoanApplication application = null;
        try{
            purchase = purchaseService.subscribed(investorId, applicationId,"123456");
            application = applicationService.getApplicationById(applicationId);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        List<RepayPlan> repayPlans = purchase.getRepayPlans();
        for (RepayPlan repayPlan : repayPlans) {
            System.out.println(repayPlan);
        }
        Assert.assertNotNull(purchase);
        Assert.assertEquals(LoanStatus.SUBSCRIBED.getStatus(), application.getStatus());
    }

    @Test
    @Transactional
    public void subscribed_fail() throws Exception{
//        String investorId  ="201630419704";
        applicationService.expire(application.getApplicationId());
        Integer applicationId = application.getApplicationId();
        expectedEx.expect(LoanStatusException.class);
        expectedEx.expectMessage("借款申请未发布");
        purchaseService.subscribed(investorId, applicationId,"123456");
    }

    @Test
    @Transactional
    public void purchaseOverdue() {
//        String investorId  ="201630419704";
        Integer applicationId = 61;
        Purchase purchase=null;
        Boolean result = false;
        try{
            purchase = purchaseService.subscribed(investorId, applicationId,"123456");
            result = purchaseService.purchaseOverdue(purchase.getPurchaseId());
            purchase = purchaseService.showPurchaseById(purchase.getPurchaseId());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        Assert.assertTrue(result);
        Assert.assertEquals(LoanStatus.OVERDUE.getStatus(), purchase.getStatus());
    }

    @Test
    @Transactional
    public void showAllPurchase() {
//        String investorId  ="201630419704";
        PageInfo<Purchase> purchasePageInfo =null;
        try{
            purchaseService.subscribed(investorId, 51,"123456");
            purchaseService.subscribed(investorId, 61,"123456");
            purchaseService.subscribed(investorId, 72,"123456");
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        try{
            purchasePageInfo = purchaseService.showAllPurchase(1, 10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        Assert.assertNotNull(purchasePageInfo);
        Assert.assertEquals(1,purchasePageInfo.getPageNum());
        Assert.assertEquals(10, purchasePageInfo.getPageSize());
        Assert.assertEquals(3, purchasePageInfo.getSize());
    }

    @Test
    @Transactional
    public void showPurchaseById() {

    }

    @Test
    @Transactional
    public void showPurchaseByBorrowerId() {
//        String investorId  ="201630419704";
        PageInfo<Purchase> purchasePageInfo =null;
        try{
            purchaseService.subscribed(investorId, 51,"123456");
            purchaseService.subscribed(investorId, 61,"123456");
            purchaseService.subscribed(investorId, 72,"123456");
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        try{
            purchasePageInfo = purchaseService.showPurchaseByBorrowerId("201630664195",1, 10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        Assert.assertNotNull(purchasePageInfo);
        Assert.assertEquals(1,purchasePageInfo.getPageNum());
        Assert.assertEquals(10, purchasePageInfo.getPageSize());
        Assert.assertEquals(3, purchasePageInfo.getSize());
    }

    @Test
    @Transactional
    public void showPurchaseByInvestorId() {
//        String investorId  ="201630419704";
        PageInfo<Purchase> purchasePageInfo =null;
        try{
            purchaseService.subscribed(investorId, 51,"123456");
            purchaseService.subscribed(investorId, 61,"123456");
            purchaseService.subscribed(investorId, 72,"123456");
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        try{
            purchasePageInfo = purchaseService.showPurchaseByInvestorId(investorId,1, 10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        Assert.assertNotNull(purchasePageInfo);
        Assert.assertEquals(1,purchasePageInfo.getPageNum());
        Assert.assertEquals(10, purchasePageInfo.getPageSize());
        Assert.assertEquals(3, purchasePageInfo.getSize());
    }

    @Test
    @Transactional
    public void accomplishPurchase() {
//        String investorId  ="201630419704";
        Integer applicationId = 61;
        Purchase purchase=null;
        Boolean result = false;
        try{
            purchase = purchaseService.subscribed(investorId, applicationId,"123456");
            result = purchaseService.accomplishPurchase(purchase.getPurchaseId());
            purchase = purchaseService.showPurchaseById(purchase.getPurchaseId());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        Assert.assertTrue(result);
        Assert.assertEquals(LoanStatus.FINISHED.getStatus(), purchase.getStatus());
    }
}