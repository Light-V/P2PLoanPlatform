package com.scut.p2ploanplatform.service;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface P2pAccountService {
    /**
     * 查询账户余额
     * @param userId 用户ID
     * @return balance 账户余额
     */
    BigDecimal showBalance(String userId);

    /**
     * 验证账户余额是否足够交易
     * @param payerId 付款人ID
     * @param amount 交易金额
     * @return 余额是否足够交易
     */
    Boolean verifyTrade(String payerId, BigDecimal amount);

    /**
     * 付款人支付
     * @param payerId 付款人ID
     * @param amount 支付金额
     * @return 操作状态（成功／失败）
     */
    Boolean pay(String payerId, BigDecimal amount);

    /**
     * 收款人收钱
     * @param payeeId 收款人ID
     * @param amount 收款金额
     * @return 操作状态（成功／失败）
     */
    Boolean income(String payeeId, BigDecimal amount);

    /**
     * 充值
     * @param userId 用户ID
     * @param cardId 银行卡ID
     * @param amount 充值金额
     * @return 操作状态（成功／失败）
     */
    Boolean recharge(String userId, String cardId, BigDecimal amount);

    /**
     * 提现
     * @param userId 用户ID
     * @param cardId 银行卡ID
     * @param amount 提现金额
     * @return 操作状态（成功／失败）
     */
    Boolean withdraw(String userId, String cardId, BigDecimal amount);
}
