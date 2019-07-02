package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class RepayPlan implements Serializable {
    private String planId;
    private Integer purchaseId;
    private Date repayDate;
    private Date realRepayDate;
    private BigDecimal amount;
    private Integer status;
    private boolean overdueProceeded;
}
