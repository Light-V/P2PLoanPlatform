package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.entity.P2pAccount;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface BankAccountService {
    /**
     * 根据卡号查询余额
     * @param cardId 卡号
     * @return 余额
     */
    BigDecimal showBalanceByCardId(String cardId) throws SQLException,IllegalArgumentException;

    /**
     * 查询特定用户所拥有的银行卡
     * @param userId 用户ID
     * @return 该用户拥有的所有银行卡账户
     */
    List<BankAccount> showCardsByUserId(String userId) throws SQLException,IllegalArgumentException;

}
