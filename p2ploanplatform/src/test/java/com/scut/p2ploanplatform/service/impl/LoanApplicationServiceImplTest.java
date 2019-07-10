package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoanApplicationServiceImplTest {

    private LoanApplication application = new LoanApplication();

    @Autowired
    LoanApplicationService applicationService;

    @Autowired
    LoanApplicationServiceImpl loanApplicationService;

    @Before
    public void newApplication(){
        application.setBorrowerId("201601000000");
        application.setTitle("我是一只冷漠的测试鸽子");
        application.setStatus(LoanStatus.UNREVIEWED.getStatus());
        application.setAmount(new BigDecimal(5000));
        application.setInterestRate(new BigDecimal(0.0618));
        application.setLoanMonth(6);
        application.setPurchaseDeadline(Calendar.getInstance().getTime());
    }

    @Test
    @Transactional
    public void addApplication() {
        Boolean result = false;
        try{
        result = applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void reviewPass() {
        Boolean result;
        String guarantorId = "201602000000";
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        result =false;
        try{
            result = applicationService.reviewPass(application.getApplicationId(),guarantorId);
            application = applicationService.getApplicationById(application.getApplicationId());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Assert.assertTrue(result);
        Assert.assertEquals(LoanStatus.REVIEWED_PASSED.getStatus(), application.getStatus());
        Assert.assertEquals(guarantorId, application.getGuarantorId());
    }

    @Test
    @Transactional
    public void reviewReject() {

        String guarantorId = "201602000000";
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        Boolean result = false;
        try{
            result = applicationService.reviewReject(application.getApplicationId(),guarantorId);
            application = applicationService.getApplicationById(application.getApplicationId());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Assert.assertTrue(result);
        Assert.assertEquals(LoanStatus.REVIEWED_REJECTED.getStatus(), application.getStatus());
        Assert.assertEquals(guarantorId, application.getGuarantorId());
    }

    @Test
    @Transactional
    public void subscribe() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Boolean result = false;

        try{
            result = applicationService.subscribe(application.getApplicationId());
            application = applicationService.getApplicationById(application.getApplicationId());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Assert.assertTrue(result);
        Assert.assertEquals(LoanStatus.SUBSCRIBED.getStatus(), application.getStatus());
    }

    @Test
    @Transactional
    public void expire() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Boolean result = false;

        try{
            result = applicationService.expire(application.getApplicationId());
            application = applicationService.getApplicationById(application.getApplicationId());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Assert.assertTrue(result);
        Assert.assertEquals(LoanStatus.EXPIRED.getStatus(), application.getStatus());
    }

    @Test
    @Transactional
    public void changeStatusById() {
    }

    @Test
    @Transactional
    public void modifyApplication() {
    }

    @Test
    @Transactional
    public void deleteApplicationById() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Boolean result = false;

        try{
            result = applicationService.deleteApplicationById(application.getApplicationId());
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        Assert.assertTrue(result);
    }

    @Test
    @Transactional
    public void getApplicationById() {
        LoanApplication application1 = new LoanApplication();
        try{
            application1=applicationService.getApplicationById(22);
        }catch (Exception e){
            fail();
        }

        Assert.assertNotNull(application1);

    }

    @Test
    @Transactional
    public void getApplicationByBorrowerId() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo= applicationService.getApplicationByBorrowerId("201601000000",1,10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        assertNotNull(applicationPageInfo);
        assertEquals(1, applicationPageInfo.getPageNum());
        assertEquals(10, applicationPageInfo.getPageSize());
    }

    @Test
    @Transactional
    public void getApplicationByGuarantorId() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo= applicationService.getApplicationByGuarantorId("201630666999",1,10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        assertNotNull(applicationPageInfo);
        assertEquals(1, applicationPageInfo.getPageNum());
        assertEquals(10, applicationPageInfo.getPageSize());
    }

    @Test
    @Transactional
    public void getApplicationByBorrowerId1() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo= applicationService.getApplicationByBorrowerId("201601000000",3,1,10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        assertNotNull(applicationPageInfo);
        assertEquals(1, applicationPageInfo.getPageNum());
        assertEquals(10, applicationPageInfo.getPageSize());
    }

    @Test
    @Transactional
    public void getApplicationByGuarantorId1() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo= applicationService.getApplicationByGuarantorId("201630664195",3,1,10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        assertNotNull(applicationPageInfo);
        assertEquals(1, applicationPageInfo.getPageNum());
        assertEquals(10, applicationPageInfo.getPageSize());
    }

    @Test
    @Transactional
    public void getApplicationReviewedPassed() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo= applicationService.getApplicationReviewedPassed(1, 10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        assertNotNull(applicationPageInfo);
        assertEquals(1, applicationPageInfo.getPageNum());
        assertEquals(10, applicationPageInfo.getPageSize());
    }

    @Test
    @Transactional
    public void getApplicationUnReviewed() {
        try{
            applicationService.addApplication(application);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo= applicationService.getApplicationUnReviewed(1, 10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

        assertNotNull(applicationPageInfo);
        assertEquals(1, applicationPageInfo.getPageNum());
        assertEquals(10, applicationPageInfo.getPageSize());
    }


    @Test
    @Transactional
    public void manualExpire() throws Exception {
        DateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        application.setStatus(LoanStatus.REVIEWED_PASSED.getStatus());
        Date date = pattern.parse("2019-6-26");
        application.setPurchaseDeadline(date);
        loanApplicationService.addApplication(application);
        Integer expireId = application.getApplicationId();

        Date today = new Date();
        String todayFormat = pattern.format(today);
        today = pattern.parse(todayFormat);
        application.setPurchaseDeadline(today);
        loanApplicationService.addApplication(application);
        Integer newId = application.getApplicationId();

        loanApplicationService.manualExpire();
        LoanApplication loanApplication = loanApplicationService.getApplicationById(expireId);
        Assert.assertEquals(LoanStatus.EXPIRED.getStatus(), loanApplication.getStatus());
        loanApplication = loanApplicationService.getApplicationById(newId);
        Assert.assertEquals(LoanStatus.REVIEWED_PASSED.getStatus(), loanApplication.getStatus());

    }

    @Test
    @Transactional
    public void getAllApplication() throws SQLException, InterruptedException {
//        for(int i =0;i<20;i++){
//            try{
//                applicationService.addApplication(application);
//            }catch (Exception e){
//                e.printStackTrace();
//                fail();
//            }
//        }
        applicationService.reviewPass(1, "201602000000");
        sleep(500);
        applicationService.reviewPass(3, "201602000000");
        sleep(500);
        applicationService.reviewPass(5, "201602000000");
        sleep(500);
        applicationService.reviewReject(8, "201602000000");
        sleep(500);
        applicationService.reviewReject(6, "201602000000");
        sleep(500);
        applicationService.reviewReject(4, "201602000000");
        sleep(500);
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo= applicationService.getAllApplication(1,10);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
        for(LoanApplication application:applicationPageInfo.getList()){
            System.out.println(application);
        }
        assertNotNull(applicationPageInfo);
        assertEquals(1, applicationPageInfo.getPageNum());
        assertEquals(10, applicationPageInfo.getPageSize());
    }
}