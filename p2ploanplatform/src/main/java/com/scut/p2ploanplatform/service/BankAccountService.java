package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.entity.P2pAccount;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface BankAccountService {
    /**
     * 添加银行卡账户
     * @param cardId 卡号
     * @param thirdPartyId 第三方ID
     * @param paymentPassword 支付密码
     * @param balance 余额
     * @return 成功返回1，失败返回0
     */
    int addBankAccount(String cardId, String thirdPartyId, String paymentPassword, BigDecimal balance) throws SQLException,IllegalArgumentException;

    /**
     * 根据卡号查询余额
     * @param cardId 卡号
     * @return 余额
     */
    BigDecimal findBalanceByCardId(String cardId) throws SQLException,IllegalArgumentException;

    /**
     * 查询特定用户所拥有的银行卡
     * @param thirdPartyId 第三方ID
     * @return 该用户拥有的银行卡账户
     */
    BankAccount findCardByThirdPartyId(String thirdPartyId) throws SQLException,IllegalArgumentException;

}
