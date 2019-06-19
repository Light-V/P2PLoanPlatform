package com.scut.p2ploanplatform.service;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.WaterBill;
import com.scut.p2ploanplatform.enums.PayModeEnum;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/14 15:52
 */
public interface WaterBillService {

    /**
     *  增加新的流水记录
     * @param payeeId 收款人id
     * @param payerId 支付人id
     * @param amount 金额
     * @param payModeEnum 付款模式，具体见{@link com.scut.p2ploanplatform.enums.PayModeEnum}
     * @param time 交易时间
     * @return 记录成功返回1，否则返回0
     */
    int addNewWaterBill(String payeeId, String payerId, BigDecimal amount, PayModeEnum payModeEnum, Date time);

    /**
     * 查找用户支出记录
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 每页数目
     * @return PageInfo<WaterBill>
     */
    PageInfo<WaterBill> findUserPayRecord(String userId, Integer pageNum, Integer pageSize);

    /**
     * 查找用户收款记录
     * @param userId 用户id
     * @param pageNum 页码
     * @param pageSize 每页数目
     * @return PageInfo<WaterBill>
     */
    PageInfo<WaterBill> findUserIncomeRecord(String userId, Integer pageNum, Integer pageSize);

    /**
     * 根据交易的模式和时间查找记录，主要用于p2p平台统计贷款和还款记录
     * @param payModeEnum 付款模式，具体见{@link com.scut.p2ploanplatform.enums.PayModeEnum}
     * @param start 起始时间, 格式 yyyy-MM-dd
     * @param end 结束时间, 格式 yyyy-MM-dd
     * @return PageInfo<WaterBill>
     */
    List<WaterBill> findWaterBillByModeAndTime(PayModeEnum payModeEnum, String start, String end);
}
