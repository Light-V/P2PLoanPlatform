package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.P2pAccount;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface P2pAccountService {
    /**
     * 新建账户
     * @param userId 用户ID
     * @param name 姓名
     * @param paymentPassword 支付密码
     * @param balance 余额
     * @param status 账户状态
     * @param type 账户类型
     * @return 成功返回1，失败返回0
     */
    int insertP2pAccount(String userId, String name, String paymentPassword, BigDecimal balance, Integer status, Integer type) throws SQLException,IllegalArgumentException;
    /**
     * 查询账户余额
     * @param userId 用户ID
     * @return balance 账户余额
     */
    BigDecimal showBalance(String userId) throws SQLException,IllegalArgumentException;

    /**
     * 验证账户余额是否足够交易
     * @param payerId 付款人ID
     * @param amount 交易金额
     * @return 余额是否足够交易
     */
    Boolean verifyTrade(String payerId, BigDecimal amount) throws SQLException,IllegalArgumentException;

    /**
     * 转账
     * @param payerId 付款人
     * @param payeeId 收款人
     * @param amount 金额
     * @return 操作状态（成功／失败）
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    Boolean transfer(String payerId, String payeeId, BigDecimal amount) throws SQLException,IllegalArgumentException;

    /**
     * 充值
     * @param userId 用户ID
     * @param cardId 银行卡ID
     * @param amount 充值金额
     * @return 操作状态（成功／失败）
     */
    Boolean recharge(String userId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException;

    /**
     * 提现
     * @param userId 用户ID
     * @param cardId 银行卡ID
     * @param amount 提现金额
     * @return 操作状态（成功／失败）
     */
    Boolean withdraw(String userId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException;
}
