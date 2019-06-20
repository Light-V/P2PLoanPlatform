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
    P2pAccountDao p2pAccountDao;

    @Autowired
    BankAccountDao bankAccountDao;

    @Override
    public int insertP2pAccount(String userId, String name, String paymentPassword, BigDecimal balance, Integer status, Integer type) throws SQLException,IllegalArgumentException
    {
        if (p2pAccountDao.findByUserId(userId)!=null)
            return 0;
        else
        {
            P2pAccount p2pAccount=new P2pAccount();
            p2pAccount.setUserId(userId);
            p2pAccount.setName(name);
            p2pAccount.setPaymentPassword(paymentPassword);
            p2pAccount.setBalance(balance);
            p2pAccount.setStatus(status);
            p2pAccount.setType(type);
            p2pAccountDao.insertP2pAccount(p2pAccount);
            return 1;
        }
    }

    @Override
    public BigDecimal showBalance(String userId) throws SQLException,IllegalArgumentException {return p2pAccountDao.findBalanceByUserId(userId);}

    @Override
    public Boolean verifyTrade(String payerId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        BigDecimal zero=new BigDecimal(0);
        if (amount.compareTo(zero)==-1)
            return false;
        BigDecimal balance=showBalance(payerId);
        if (balance.compareTo(amount)==0 || balance.compareTo(amount)==1)
            return true;
        else
            return false;
    }

    @Override
    public Boolean transfer(String payerId, String payeeId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        if (verifyTrade(payerId, amount))
        {
            BigDecimal payerBalance=showBalance(payerId);
            BigDecimal payeeBalance=showBalance(payeeId);
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
    public Boolean recharge(String userId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        if (verifyTrade(userId,amount))
        {
            BigDecimal p2pBalance=showBalance(userId);
            BigDecimal cardBalance=bankAccountDao.findBalanceByCardId(cardId);
            BigDecimal newP2pBalance=p2pBalance.subtract(amount);
            BigDecimal newCardBalance=cardBalance.add(amount);
            p2pAccountDao.updateBalance(userId, newP2pBalance);
            bankAccountDao.updateBalance(cardId, newCardBalance);
            return true;
        }
        else
            return false;
    }

    @Override
    public Boolean withdraw(String userId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        BigDecimal zero=new BigDecimal(0);
        if (amount.compareTo(zero)==-1)
            return false;
        BigDecimal cardBalance=bankAccountDao.findBalanceByCardId(cardId);
        if (cardBalance.compareTo(amount)==0 || cardBalance.compareTo(amount)==1)
        {
            BigDecimal p2pBalance=showBalance(userId);
            BigDecimal newP2pBalance=p2pBalance.add(amount);
            BigDecimal newCardBalance=cardBalance.subtract(amount);
            p2pAccountDao.updateBalance(userId,newP2pBalance);
            bankAccountDao.updateBalance(cardId,newCardBalance);
            return true;
        }
        else
            return false;
    }

}
