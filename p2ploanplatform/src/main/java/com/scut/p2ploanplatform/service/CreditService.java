package com.scut.p2ploanplatform.service;


import com.scut.p2ploanplatform.entity.CreditInfo;
//import com.sun.org.apache.xpath.internal.operations.Bool;

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
     * @param creditInfo 更新的征信信息
     * @return true(信息被修改)/false(信息未修改)
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    boolean updateCreditInfo(CreditInfo creditInfo) throws SQLException, IllegalArgumentException;
}
