package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * P2P平台账户
 */
@Data
public class P2pAccount {
    /**
     * 用户ID
     */
    private String userID;
    /**
     * 用户姓名
     */
    private String name;
    /**
     *支付密码
     */
    private String paymentPassword;
    /**
     * 账户余额
     */
    private BigDecimal balance;
    /**
     * 账户状态
     * 0表示冻结，1表示正常
     */
    private int status;
    /**
     * 账户类型
     * 0表示普通账户，1表示风险准备金账户
     */
    private int type;
}