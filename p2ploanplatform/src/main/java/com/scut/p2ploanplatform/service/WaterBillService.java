package com.scut.p2ploanplatform.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zonghang
 * Date 2019/6/20 15:04
 */
public interface WaterBillService {

    /**
     * 增加贷款记录
     * @param purchaseId 认购id
     * @param borrowerId 借款人id
     * @param investorId 投资人id
     * @param time 时间
     * @param amount 金额
     */
    void addLoanRecord(Integer purchaseId, String borrowerId, String investorId, Date time, BigDecimal amount);

    /**
     * 增加还款记录
     * @param planId 还款计划id
     * @param purchaseId 认购id
     * @param payerId 支付人id
     * @param payeeId 收款人id
     * @param time 时间
     * @param amount 金额
     */
    void addRepayRecord(String planId, Integer purchaseId, String payerId, String payeeId, Date time, BigDecimal amount);
}
