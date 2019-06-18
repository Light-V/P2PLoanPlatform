package com.scut.p2ploanplatform.service;


import java.sql.SQLException;

/**
 * @author: Light
 * @date: 2019/6/17 16:35
 * @description:处理征信授信业务
 */

public interface CreditService {

    /**
     * 通过用户Id对用户进行征信
     * @param userId 被征信的用户Id
     * @return 用户的征信系数
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    Double creditReport(String userId) throws SQLException, IllegalArgumentException;

    /**
     * 通过用户Id对用户进行授信
     * @param userId 需要授信的用户Id
     * @return 用户的最大借款金额
     * @throws SQLException SQL异常
     * @throws  IllegalArgumentException 非法参数
     */
    Double creditGarant(String userId) throws  SQLException, IllegalArgumentException;



}
