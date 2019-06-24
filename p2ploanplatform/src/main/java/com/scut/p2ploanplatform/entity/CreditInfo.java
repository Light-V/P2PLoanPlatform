package com.scut.p2ploanplatform.entity;

import com.scut.p2ploanplatform.form.CreditInfoForm;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: Light
 * @date: 2019/6/17 10:59
 * @description:
 */

@Data
public class CreditInfo {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 收入（单位:人民币）
     */
    private BigDecimal income;

    /**
     * 家庭收入 （单位:人民币）
     */
    private BigDecimal familyIncome;

    /**
     *资产 （单位:人民币）
     */
    private BigDecimal assets;

    /**
     * 家庭成员人数
     */
    private Integer familyNumber;

    /**
     * 负载 （单位:人民币）
     */
    private BigDecimal debt;

    /**
     * 信用分数
     */
    private Integer creditScore;

    public CreditInfo(){}

    public CreditInfo(String userId, CreditInfoForm creditInfoForm){
        this.setUserId(userId);
        this.setFamilyIncome(creditInfoForm.getFamilyIncome());
        this.setCreditScore(100);
        this.setIncome(creditInfoForm.getIncome());
        this.setFamilyNumber(creditInfoForm.getFamilyNumber());
        this.setAssets(creditInfoForm.getAssets());
        this.setDebt(creditInfoForm.getDebt());
    }
}
