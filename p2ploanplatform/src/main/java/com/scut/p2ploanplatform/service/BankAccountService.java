package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.entity.P2pAccount;

import java.math.BigDecimal;
import java.util.List;

public interface BankAccountService {
    /**
     * 根据卡号查询余额
     * @param cardId 卡号
     * @return 余额
     */
    BigDecimal showBalanceByCardId(String cardId);

    /**
     * 查询特定用户所拥有的银行卡
     * @param userId 用户ID
     * @return 该用户拥有的所有银行卡账户
     */
    List<BankAccount> showCardsByUserId(String userId);

    /**
     * 提现
     * @param cardId 卡号
     * @param amount 提现金额
     * @return 操作状态（成功／失败）
     */
    Boolean withdraw(String cardId, BigDecimal amount);

    /**
     * 充值
     * @param cardId 卡号
     * @param amount 充值金额
     * @return 操作状态（成功／失败）
     */
    Boolean recharge(String cardId, BigDecimal amount);
}
