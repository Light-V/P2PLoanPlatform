package com.scut.p2ploanplatform.dto;

import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.enums.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserHistory {
    private Integer purchaseId;
    private BigDecimal money;
    private Date time;
    private String isOverdue;
    public UserHistory(){};
    public UserHistory(Purchase purchase){
        this.purchaseId = purchase.getPurchaseId();
        this.money = purchase.getAmount();
        this.time = purchase.getPurchaseTime();
        if(purchase.getStatus().equals(LoanStatus.OVERDUE.getStatus())){
            this.isOverdue = "是";
        }
        else this.isOverdue = "否";
    }
}
