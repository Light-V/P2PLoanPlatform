package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.LoanApplication;
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

import static java.lang.Thread.sleep;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanApplicationDaoTest {

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    private LoanApplication loanApplication = new LoanApplication();

    @Before
    public void newApplication(){
        loanApplication.setBorrowerId("201630664195");
        loanApplication.setGuarantorId("201630219652");
        loanApplication.setTitle("撒娇打滚求借钱");
        loanApplication.setStatus(LoanStatus.UNREVIEWED.getStatus());
        loanApplication.setAmount(new BigDecimal(1000000));
        loanApplication.setInterestRate(new BigDecimal(0.0618));
        loanApplication.setLoanMonth(3);
        loanApplication.setPurchaseDeadline(Calendar.getInstance().getTime());
    }


    @Test
    @Transactional
    public void addApplication() {
        Boolean result = loanApplicationDao.addApplication(loanApplication);
        Assert.assertTrue( result);
    }

    @Test
    @Transactional
    public void updateApplication() {
        loanApplicationDao.addApplication(loanApplication);
        loanApplication.setStatus(LoanStatus.REVIEWED_PASSED.getStatus());
        Boolean result = loanApplicationDao.updateApplication(loanApplication);
        Assert.assertTrue( result);
    }

    @Test
    public void modifyApplication() {
    }

    @Test
    @Transactional
    public void deleteApplicationById() {
        loanApplicationDao.addApplication(loanApplication);
        Integer applicationId = loanApplication.getApplicationId();
        Boolean result = loanApplicationDao.deleteApplicationById(applicationId);
        Assert.assertTrue( result);
    }

    @Test
    @Transactional
    public void showApplicationByApplicationId() {
        loanApplicationDao.addApplication(loanApplication);
        Integer applicationId = loanApplication.getApplicationId();
        LoanApplication application = loanApplicationDao.showApplicationById(applicationId);
        Assert.assertEquals(loanApplication.getTitle(), application.getTitle());
    }

    @Test
    @Transactional
    public void showApplicationByBorrowerId() {
        List<LoanApplication> application;
        application = loanApplicationDao.showApplicationByBorrowerId("201630664195");
        Assert.assertEquals(9, application.size());
    }

    @Test
    @Transactional
    public void showApplicationByGuarantorId() {
        loanApplicationDao.addApplication(loanApplication);
        List<LoanApplication> application;
        application = loanApplicationDao.showApplicationByGuarantorId("201630219652");
        Assert.assertEquals(10, application.size());
    }

    @Test
    @Transactional
    public void showApplicationByBorrowerIdAndStatus() {
        List<LoanApplication> application;
        application = loanApplicationDao.showApplicationByBorrowerIdAndStatus("201630664195",LoanStatus.UNREVIEWED.getStatus());
        Assert.assertEquals(6, application.size());
    }

    @Test
    @Transactional
    public void showApplicationByGuarantorIdAndStatus() {
        List<LoanApplication> application;
        application = loanApplicationDao.showApplicationByGuarantorIdAndStatus("201630219652",LoanStatus.REVIEWED_PASSED.getStatus());
        Assert.assertEquals(3, application.size());
    }

    @Test
    @Transactional
    public void showApplicationReviewedPassed() {
        List<LoanApplication> application;
        application = loanApplicationDao.showApplicationReviewedPassed();
        Assert.assertEquals(3, application.size());
    }
}