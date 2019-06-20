package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.CreditInfoDao;
import com.scut.p2ploanplatform.dao.GrantCreditDao;
import com.scut.p2ploanplatform.entity.CreditInfo;
import com.scut.p2ploanplatform.entity.GrantCredit;
import com.scut.p2ploanplatform.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
        if (!creditInfo.isComplete()) return new BigDecimal(-1.);

        //征信信息填写完整,返回征信系数
        return new BigDecimal(creditInfo.getCreditScore().toString()).divide(new BigDecimal("50"));

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
            grantCredit.setQuota(grantCredit.getIncome().multiply(rate));
            grantCreditDao.updateGrantCredit(grantCredit);
        }
        return grantCredit.getRate().multiply(rate);
    }


    /**
     * 更新用户征信信息
     * @param creditInfo 更新的征信信息
     * @return true(信息被修改)/false(信息未修改)
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    @Override
    public boolean updateCreditInfo(CreditInfo creditInfo) {
        CreditInfo result = creditInfoDao.selectCreditInfo(creditInfo.getUserId());
        if (result != null){
            //exist old credit information
            creditInfoDao.updateCreditInfo(creditInfo);
            if (result.equals(creditInfo)) {
                //credit information is not modified
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
            creditInfoDao.insertCreditInfo(creditInfo);
            return false;
        }
    }

    private Date offsetOneMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }
}
