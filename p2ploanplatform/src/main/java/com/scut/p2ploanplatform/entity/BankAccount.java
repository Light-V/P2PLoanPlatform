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
     * 第三方ID
     */
    private String thirdPartyId;
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

    public String getThirdPartyId() { return thirdPartyId; }

    public BigDecimal getBalance()
    {
        return balance;
    }

    public String getPaymentPassword() { return paymentPassword; }

    public void setThirdPartyId(String thirdPartyId) { this.thirdPartyId = thirdPartyId; }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setCardID(String cardID) {
        this.cardId = cardID;
    }

    public void setPaymentPassword(String paymentPassword) {
        this.paymentPassword = paymentPassword;
    }
}
