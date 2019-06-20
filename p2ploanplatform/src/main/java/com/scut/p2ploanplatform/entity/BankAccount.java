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

    public String getCardID()
    {
        return cardID;
    }

    public String getUserID()
    {
        return userID;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public String getPaymentPassword() { return paymentPassword; }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCardID(String cardID) {
        this.cardID = cardID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }
}
