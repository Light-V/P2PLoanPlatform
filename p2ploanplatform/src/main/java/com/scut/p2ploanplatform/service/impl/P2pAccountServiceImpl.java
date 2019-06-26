package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.BankAccountDao;
import com.scut.p2ploanplatform.dao.P2pAccountDao;
import com.scut.p2ploanplatform.entity.P2pAccount;
import com.scut.p2ploanplatform.service.P2pAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class P2pAccountServiceImpl implements P2pAccountService {

    @Autowired
    private P2pAccountDao p2pAccountDao;

    @Autowired
    private BankAccountDao bankAccountDao;

    @Override
    public int addP2pAccount(String thirdPartyId, String paymentPassword, BigDecimal balance, Integer status, Integer type) throws SQLException,IllegalArgumentException
    {
        if (balance.compareTo(BigDecimal.ZERO)<0||(status!=1&&status!=0)||(type!=1&&type!=0))
            throw new IllegalArgumentException("非法参数！");
        if (verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException("id为%s的账户已经存在！");
        try
        {
            P2pAccount p2pAccount=new P2pAccount();
            p2pAccount.setThirdPartyId(thirdPartyId);
            p2pAccount.setPaymentPassword(paymentPassword);
            p2pAccount.setBalance(balance);
            p2pAccount.setStatus(status);
            p2pAccount.setType(type);
            p2pAccountDao.addP2pAccount(p2pAccount);
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
        return 1;
    }

    @Override
    public BigDecimal findBalance(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为%s的账户还未创建！",thirdPartyId));
        try
        {
            return p2pAccountDao.findBalanceByThirdPartyId(thirdPartyId);
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    @Override
    public Boolean verifyIfExists(String thirdPartyId)
    {
        if (p2pAccountDao.findByThirdPartyId(thirdPartyId)==null)
            return false;
        else
            return true;
    }

    @Override
    public Boolean verifyTrade(String payerId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(payerId))
            throw new IllegalArgumentException(String.format("id为%s的账户还未创建！",payerId));
        if (amount.compareTo(BigDecimal.ZERO)<0)
            throw new IllegalArgumentException("金额必须为正数！");
        BigDecimal balance=findBalance(payerId);
        if (balance.compareTo(amount)>=0)
            return true;
        else
            return false;
    }

    @Override
    public Boolean verifyPassword(String thirdPartyId, String password) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为%s的账户还未创建！",thirdPartyId));
        try
        {
            return password.equals(p2pAccountDao.findPasswordByThirdPartyId(thirdPartyId));
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    @Override
    public Boolean transfer(String payerId, String payeeId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        if (verifyTrade(payerId, amount))
        {
            BigDecimal payerBalance=findBalance(payerId);
            BigDecimal payeeBalance=findBalance(payeeId);
            BigDecimal newPayerBalance=payerBalance.subtract(amount);
            BigDecimal newPayeeBalance=payeeBalance.add(amount);
            p2pAccountDao.updateBalance(payerId, newPayerBalance);
            p2pAccountDao.updateBalance(payeeId, newPayeeBalance);
            return true;
        }
        else
            return false;
    }

    @Override
    public Boolean verifyPasswordIsSet(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为%s的账户还未创建！",thirdPartyId));
        try
        {
            String password=p2pAccountDao.findPasswordByThirdPartyId(thirdPartyId);
            return !password.equals("");
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    @Override
    public Boolean recharge(String thirdPartyId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        if (amount.compareTo(BigDecimal.ZERO)<0)
            throw new IllegalArgumentException("金额必须为正数！");
        BigDecimal cardBalance=bankAccountDao.findBalanceByCardId(cardId);
        if (cardBalance.compareTo(amount)>=0)
        {
            BigDecimal p2pBalance=findBalance(thirdPartyId);
            BigDecimal newCardBalance=cardBalance.subtract(amount);
            BigDecimal newP2pBalance=p2pBalance.add(amount);
            p2pAccountDao.updateBalance(thirdPartyId, newP2pBalance);
            bankAccountDao.updateBalance(cardId, newCardBalance);
            return true;
        }
        else
            return false;
    }

    @Override
    public Boolean withdraw(String thirdPartyId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        if (amount.compareTo(BigDecimal.ZERO)<0)
            throw new IllegalArgumentException("金额必须为正数！");
        BigDecimal p2pBalance=findBalance(thirdPartyId);
        if (p2pBalance.compareTo(amount)>=0)
        {
            BigDecimal cardBalance=bankAccountDao.findBalanceByCardId(cardId);
            BigDecimal newP2pBalance=p2pBalance.subtract(amount);
            BigDecimal newCardBalance=cardBalance.add(amount);
            p2pAccountDao.updateBalance(thirdPartyId,newP2pBalance);
            bankAccountDao.updateBalance(cardId,newCardBalance);
            return true;
        }
        else
            return false;
    }
}
