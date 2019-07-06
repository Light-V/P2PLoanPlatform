package com.scut.p2ploanplatform.service.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.exception.LoanStatusException;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.service.P2pAccountService;
import com.scut.p2ploanplatform.service.RepayService;
import com.scut.p2ploanplatform.utils.ThirdPartyOperationInterface;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class PurchaseServiceImplTest {
    @LocalServerPort
    private int port;

    @Autowired
    private PurchaseServiceImpl purchaseService;

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private P2pAccountServiceImpl p2pAccountService;

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    private List<LoanApplication> applications = new ArrayList<>();
    private String investorId = "201603000000";

    @Before
    public void newApplication() throws Exception{
        for(int i=1; i<5; i++) {
            LoanApplication application =  new LoanApplication();
            application.setBorrowerId("201601000000");
            application.setTitle("测试");
            application.setStatus(LoanStatus.UNREVIEWED.getStatus());
            application.setAmount(new BigDecimal(i*2000));
            application.setInterestRate(new BigDecimal(0.0527));
            application.setLoanMonth(i*3);
            application.setPurchaseDeadline(Calendar.getInstance().getTime());
            applicationService.addApplication(application);
            applicationService.reviewPass(application.getApplicationId(), "201602000000");
            applications.add(application);
        }

        if (p2pAccountService.getApiKey() == null) {
            p2pAccountService.generateApiKey();
        }
        ThirdPartyOperationInterface.setApiKey(p2pAccountService.getApiKey());
        ThirdPartyOperationInterface.setThirdPartyApiUrl("http://localhost:" + port + "/third_party/");
    }

    @Test
    @Transactional
    public void subscribed_success() {
        Integer applicationId = applications.get(1).getApplicationId();
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
    public void purchaseOverdue() {
        Integer applicationId = applications.get(3).getApplicationId();
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
        PageInfo<Purchase> purchasePageInfo =null;
        try{
            purchaseService.subscribed(investorId, applications.get(0).getApplicationId(),"123456");
            purchaseService.subscribed(investorId, applications.get(1).getApplicationId(),"123456");
            purchaseService.subscribed(investorId, applications.get(2).getApplicationId(),"123456");
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
    public void showPurchaseByBorrowerId() {
        PageInfo<Purchase> purchasePageInfo =null;
        try{
            purchaseService.subscribed(investorId, applications.get(2).getApplicationId(),"123456");
            purchaseService.subscribed(investorId, applications.get(3).getApplicationId(),"123456");
            purchaseService.subscribed(investorId, applications.get(1).getApplicationId(),"123456");
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        try{
            purchasePageInfo = purchaseService.showPurchaseByBorrowerId("201601000000",1, 10);
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
        PageInfo<Purchase> purchasePageInfo =null;
        try{
            purchaseService.subscribed(investorId, applications.get(0).getApplicationId(),"123456");
            purchaseService.subscribed(investorId, applications.get(2).getApplicationId(),"123456");
            purchaseService.subscribed(investorId, applications.get(1).getApplicationId(),"123456");
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
        Integer applicationId = applications.get(3).getApplicationId();
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

    @Test
    @Transactional
    public void showPurchaseByApplicationId() throws Exception{
        Purchase purchase;
        purchaseService.subscribed(investorId, applications.get(1).getApplicationId(),"123456");
        purchase = purchaseService.showPurchaseByApplicationId(applications.get(1).getApplicationId());
        Assert.assertNotNull(purchase);
        System.out.println(purchase);
    }
}