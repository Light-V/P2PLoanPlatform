package com.scut.p2ploanplatform.dto;

import com.scut.p2ploanplatform.entity.CreditInfo;
import com.scut.p2ploanplatform.entity.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserInfoGuarantorView {
    private String name;
    private String phone;
    private String address;
    private BigDecimal monthIncome;
    private BigDecimal familyIncome;
    private Integer familyNumber;
    private BigDecimal assets;
    private BigDecimal debts;
    private Integer score;
    public UserInfoGuarantorView(){}
    public UserInfoGuarantorView(CreditInfo creditInfo, User user)
    {
        this.name = user.getName();
        this.phone = user.getPhone();
        this.address = user.getAddress();
        this.monthIncome = creditInfo.getIncome();
        this.familyIncome = creditInfo.getFamilyIncome();
        this.familyNumber = creditInfo.getFamilyNumber();
        this.assets = creditInfo.getAssets();
        this.debts = creditInfo.getDebt();
        this.score = creditInfo.getCreditScore();
    }
}
