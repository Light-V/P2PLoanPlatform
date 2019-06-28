package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.BankAccount;
import java.math.BigDecimal;
import java.sql.SQLException;

public interface BankAccountService {
    /**
     * 添加银行卡账户
     * @param cardId 卡号
     * @param thirdPartyId 第三方ID
     * @param paymentPassword 支付密码
     * @param balance 余额
     * @return 成功返回1，失败返回0
     * @throws SQLException SQL查询异常
     * @throws IllegalArgumentException 非法参数异常
     */
    int addBankAccount(String cardId, String thirdPartyId, String paymentPassword, BigDecimal balance) throws SQLException,IllegalArgumentException;

    /**
     * 解绑银行卡
     * @param thirdPartyId 第三方ID
     * @param cardId 卡号
     * @return 成功返回1，失败返回0
     * @throws SQLException SQL查询异常
     * @throws IllegalArgumentException 非法参数异常
     */
    int untieBankAccount(String thirdPartyId, String cardId) throws SQLException,IllegalArgumentException;

    /**
     * 查询特定用户所拥有的银行卡
     * @param thirdPartyId 第三方ID
     * @return 该用户拥有的银行卡账户
     * @throws SQLException SQL查询异常
     * @throws IllegalArgumentException 非法参数异常
     */
    BankAccount findCardByThirdPartyId(String thirdPartyId) throws SQLException,IllegalArgumentException;

    /**
     * 检验银行卡是否已被添加
     * @param cardId 卡号
     * @return 该卡是否已被添加
     * @throws SQLException SQL查询异常
     * @throws IllegalArgumentException 非法参数异常
     */
    Boolean verifyCardIsAdded(String cardId) throws SQLException,IllegalArgumentException;

    /**
     * 检验银行卡密码是否正确
     * @param cardId 卡号
     * @param paymentPassword 支付密码
     * @return 密码是否正确
     * @throws SQLException SQL查询异常
     * @throws IllegalArgumentException 非法参数异常
     */
    Boolean verifyPassword(String cardId,String paymentPassword) throws SQLException,IllegalArgumentException;

}
