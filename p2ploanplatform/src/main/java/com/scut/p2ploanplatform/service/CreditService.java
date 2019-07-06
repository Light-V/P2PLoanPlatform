package com.scut.p2ploanplatform.service;


import com.scut.p2ploanplatform.entity.CreditInfo;
import com.scut.p2ploanplatform.form.CreditInfoForm;
import java.math.BigDecimal;
import java.sql.SQLException;

/**
 * @author Light
 * @date 2019/6/17 16:35d
 * @description 处理征信授信业务
 */

public interface CreditService {

    /**
     * 通过用户Id对用户进行征信
     * @param userId 被征信的用户Id
     * @return 用户的征信系数
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    BigDecimal creditReport(String userId) throws SQLException, IllegalArgumentException;

    /**
     * 通过用户Id对用户进行授信
     * @param userId 需要授信的用户Id
     * @return 用户的最大借款金额
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    BigDecimal creditGrant(String userId, BigDecimal rate) throws SQLException, IllegalArgumentException;

    /**
     * 通过用户Id查询用户的征信信息
     * @param userId 查询的用户Id
     * @return 用户征信信息
     * @throws SQLException SQL异常
     * @throws IllegalArgumentException 非法参数
     */
    CreditInfo getCreditInfo(String userId) throws SQLException, IllegalArgumentException;

    /**
     * 更新用户征信信息
     * @param userId 更新的Id
     * @param creditInfoForm 表单提交的征信信息
     * @return true(信息被修改)/false(信息未修改)
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    boolean updateCreditInfo(String userId, CreditInfoForm creditInfoForm) throws SQLException, IllegalArgumentException;

    /**
     * 获取已经授信的信用额度
     * @param userId 需要获取的Id
     * @return 授信额度
     * @throws SQLException SQL异常
     */
    BigDecimal getGrantInfo(String userId) throws SQLException;

    /**
     * 更新用户征信分数
     * @param userId 更新的Id
     * @param creditScore 征信分数
     * @return true(信息被修改)/false(信息未修改)
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    boolean updateCreditScore(String userId, Integer creditScore) throws SQLException, IllegalArgumentException;

    /**
     * 获得用户征信分数
     * @param userId 用户Id
     * @return 用户信用分数
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    Integer getCreditScore(String userId) throws SQLException, IllegalArgumentException;

}
