package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zonghang
 * Date 2019/6/20 21:45
 */
@Data
public class LoanRecord {

    private String recordId;

    private Integer purchaseId;

    private String borrowerId;

    private String investorId;

    private Date time;

    private BigDecimal amount;

}
