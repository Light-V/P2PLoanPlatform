package com.scut.p2ploanplatform.entity;

import java.math.BigDecimal;

/**
 * 银行账户
 */

public class BankAccount {
    /**
     * 银行卡ID
     */
    private String cardID;
    /**
     * 用户ID
     */
    private String userID;
    /**
     * 用户姓名
     */
    private String name;
    /**
     * 支付密码
     */
    private String paymentPassword;
    /**
     * 银行卡余额
     */
    private BigDecimal balance;

    /**
     * 获取银行卡ID
     * @return 卡号
     */
    public String getCardID()
    {
        return cardID;
    }

    /**
     * 获取用户ID
     * @return 用户ID
     */
    public String getUserID()
    {
        return userID;
    }

    /**
     * 获取余额
     * @return 余额
     */
    public BigDecimal getBalance()
    {
        return balance;
    }
}
