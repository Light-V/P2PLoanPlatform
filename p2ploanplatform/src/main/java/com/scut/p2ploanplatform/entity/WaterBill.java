package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zonghang
 * Date 2019/6/14 14:48
 */

@Data
public class WaterBill {

    private String waterBillId;

    private String payeeId;

    private String payerId;

    private BigDecimal amount;

    private Integer mode;

    private Date time;
}
