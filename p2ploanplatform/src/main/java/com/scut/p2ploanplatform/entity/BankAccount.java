package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 银行账户
 */
@Data
public class BankAccount {
    /**
     * 银行卡ID
     */
    private String cardId;
    /**
     * 用户ID
     */
    private String userId;
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
        return cardId;
    }

    public String getUserID()
    {
        return userId;
    }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public String getPaymentPassword() { return paymentPassword; }

    public String getName() { return name; }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCardID(String cardID) {
        this.cardId = cardID;
    }

    public void setUserID(String userID) {
        this.userId = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }
}
