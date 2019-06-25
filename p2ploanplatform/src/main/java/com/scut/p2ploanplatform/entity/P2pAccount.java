package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * P2P平台账户
 */
@Data
public class P2pAccount {
    /**
     * 第三方ID
     */
    private String thirdPartyId;
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
    private Integer status;
    /**
     * 账户类型
     * 0表示普通账户，1表示风险准备金账户
     */
    private Integer type;


    public String getThirdPartyId() {return thirdPartyId;}

    public BigDecimal getBalance()
    {
        return balance;
    }

    public Integer getStatus() { return status; }

    public Integer getType() { return type; }

    public String getPaymentPassword() { return paymentPassword; }

    public void setThirdPartyId(String thirdPartyId) {this.thirdPartyId=thirdPartyId;}

    public void setPaymentPassword(String paymentPassword) {this.paymentPassword=paymentPassword;}

    public void setBalance(BigDecimal balance) {this.balance=balance;}

    public void setStatus(Integer status) {this.status=status;}

    public void setType(Integer type) {this.type=type;}
}
