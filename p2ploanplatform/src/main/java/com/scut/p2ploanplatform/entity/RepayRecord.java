package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zonghang
 * Date 2019/6/20 21:47
 */
@Data
public class RepayRecord {

    private String recordId;

    private String planId;

    private Integer purchaseId;

    private String payerId;

    private String payeeId;

    private Date time;

    private BigDecimal amount;
}
