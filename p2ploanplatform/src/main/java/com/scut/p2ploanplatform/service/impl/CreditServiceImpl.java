package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.CreditInfoDao;
import com.scut.p2ploanplatform.dao.GrantCreditDao;
import com.scut.p2ploanplatform.entity.CreditInfo;
import com.scut.p2ploanplatform.entity.GrantCredit;
import com.scut.p2ploanplatform.form.CreditInfoForm;
import com.scut.p2ploanplatform.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 * @author: Light
 * @date: 2019/6/17 19:49
 * @description:
 */

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditInfoDao creditInfoDao;

    @Autowired
    private GrantCreditDao grantCreditDao;


    /**
     * 通过用户Id对用户进行征信
     * @param userId 被征信的用户Id
     * @return 用户的征信系数
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    @Override
    public BigDecimal creditReport(String userId) throws SQLException, IllegalArgumentException {
        //检查是否提交了征信信息
        CreditInfo creditInfo = creditInfoDao.selectCreditInfo(userId);
        //if (!creditInfo.isAllComplete()) return new BigDecimal(-1.);

        //征信信息填写完整,返回征信系数
        return new BigDecimal(creditInfo.getCreditScore().toString()).divide(new BigDecimal("50"),2, RoundingMode.HALF_UP);

    }


    /**
     * 通过用户Id查询用户的征信信息
     * @param userId 查询的用户Id
     * @return 用户征信信息
     * @throws SQLException SQL异常
     * @throws IllegalArgumentException 非法参数
     */
    @Override
    public CreditInfo getCreditInfo(String userId) throws SQLException, IllegalArgumentException {
        CreditInfo creditInfo = creditInfoDao.selectCreditInfo(userId);
        if(creditInfo == null){
            return new CreditInfo();
        }
        else{
            return creditInfo;
        }
    }

    /**
     * 通过用户Id对用户进行授信
     * @param userId 需要授信的用户Id
     * @return 用户的最大借款金额
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    @Override
    public BigDecimal creditGrant(String userId, BigDecimal rate) throws SQLException, IllegalArgumentException {
        CreditInfo creditInfo = creditInfoDao.selectCreditInfo(userId);
        if (creditInfo==null) return new BigDecimal(-1.);
        GrantCredit grantCredit = grantCreditDao.selectGrantCredit(userId);
        if (grantCredit == null) {
            //Insert grant credit
            grantCredit = new GrantCredit();
            grantCredit.setUserId(userId);
            grantCredit.setRate(rate);
            grantCredit.setQuota(creditInfo.getIncome().multiply(rate));
            grantCredit.setIncome(creditInfo.getIncome());
            grantCredit.setExpire(offsetOneMonth(new Date()));
            grantCreditDao.insertGrantCredit(grantCredit);
        }
        else {
            //Update grant credit
            grantCredit.setRate(rate);
            grantCredit.setExpire(offsetOneMonth(new Date()));
            grantCredit.setIncome(creditInfo.getIncome());
            grantCreditDao.updateGrantCredit(grantCredit);
        }
        return grantCredit.getIncome().multiply(rate);
    }


    /**
     * 更新用户征信信息
     * @param userId 更新的Id
     * @param creditInfoForm 表单提交的征信信息
     * @return true(信息被修改)/false(信息未修改)
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    @Override
    public boolean updateCreditInfo(String userId, CreditInfoForm creditInfoForm) {
        CreditInfo result = creditInfoDao.selectCreditInfo(userId);
        CreditInfo creditInfo = new CreditInfo(userId, creditInfoForm);
        if (result != null){
            //exist old credit information
            if (creditInfoDao.updateCreditInfo(creditInfo) == 1) {
                return true;
            }
            else {
                //credit information is modified, needs update

                creditInfoDao.updateCreditInfo(creditInfo);
                return false;
            }
        }
        else{
            //no credit information. insert new credit information
            creditInfo.setCreditScore(100);
            creditInfoDao.insertCreditInfo(creditInfo);
            return false;
        }
    }

    @Override
    public BigDecimal getGrantInfo(String userId) throws SQLException {
        GrantCredit grantCredit = grantCreditDao.selectGrantCredit(userId);
        if (grantCredit == null || grantCredit.getExpire().compareTo(new Date()) < 0) {
            //未授信或授信过期
            BigDecimal rate = this.creditReport(userId);
            return this.creditGrant(userId, rate);
        }
        else{
            //授信未过期,直接返回
            return grantCredit.getQuota();
        }
    }

    private Date offsetOneMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }


    @Override
    public boolean updateCreditScore(String userId, Integer creditScore) throws SQLException, IllegalArgumentException {
        int result = creditInfoDao.updateCreditScore(userId, creditScore);
        return (result == 1);
    }

    @Override
    public Integer getCreditScore(String userId) throws SQLException, IllegalArgumentException {
        return creditInfoDao.selectCreditScore(userId);
    }
}
