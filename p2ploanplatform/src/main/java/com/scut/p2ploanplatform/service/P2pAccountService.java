package com.scut.p2ploanplatform.service;

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


}
