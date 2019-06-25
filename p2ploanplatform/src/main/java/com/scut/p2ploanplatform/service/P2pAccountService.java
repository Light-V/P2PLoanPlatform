package com.scut.p2ploanplatform.service;

import java.math.BigDecimal;
import java.sql.SQLException;

public interface P2pAccountService {
    /**
     * 新建账户
     * @param thirdPartyId 第三方ID
     * @param paymentPassword 支付密码
     * @param balance 余额
     * @param status 账户状态
     * @param type 账户类型
     * @return 成功返回1，失败返回0
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    int addP2pAccount(String thirdPartyId, String paymentPassword, BigDecimal balance, Integer status, Integer type) throws SQLException,IllegalArgumentException;

    /**
     * 查询账户余额
     * @param thirdPartyId 第三方ID
     * @return balance 账户余额
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    BigDecimal findBalance(String thirdPartyId) throws SQLException,IllegalArgumentException;

    /**
     * 验证账户余额是否足够交易
     * @param payerId 付款人ID
     * @param amount 交易金额
     * @return 余额是否足够交易
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    Boolean verifyTrade(String payerId, BigDecimal amount) throws SQLException,IllegalArgumentException;

    /**
     * 检验用户是否已设置支付密码
     * @param thirdPartyId 第三方ID
     * @return 是否已设置
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    Boolean verifyPasswordIsSet(String thirdPartyId) throws SQLException,IllegalArgumentException;

    /**
     * 验证支付密码是否正确
     * @param thirdPartyId 第三方ID
     * @param password 支付密码
     * @return 正确与否
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    Boolean verifyPassword(String thirdPartyId, String password) throws SQLException,IllegalArgumentException;

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
     * @param thirdPartyId 第三方ID
     * @param cardId 银行卡ID
     * @param amount 充值金额
     * @return 操作状态（成功／失败）
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    Boolean recharge(String thirdPartyId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException;

    /**
     * 提现
     * @param thirdPartyId 第三方ID
     * @param cardId 银行卡ID
     * @param amount 提现金额
     * @return 操作状态（成功／失败）
     * @throws SQLException
     * @throws IllegalArgumentException
     */
    Boolean withdraw(String thirdPartyId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException;
}
