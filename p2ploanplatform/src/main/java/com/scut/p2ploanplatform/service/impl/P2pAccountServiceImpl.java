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
    public int addP2pAccount(String thirdPartyId, String paymentPassword, BigDecimal balance, Integer status, Integer type) throws SQLException,IllegalArgumentException
    {
        if (p2pAccountDao.findByThirdPartyId(thirdPartyId)!=null)
            return 0;
        else
        {
            P2pAccount p2pAccount=new P2pAccount();
            p2pAccount.setThirdPartyId(thirdPartyId);
            p2pAccount.setPaymentPassword(paymentPassword);
            p2pAccount.setBalance(balance);
            p2pAccount.setStatus(status);
            p2pAccount.setType(type);
            p2pAccountDao.addP2pAccount(p2pAccount);
            return 1;
        }
    }

    @Override
    public BigDecimal findBalance(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        return p2pAccountDao.findBalanceByThirdPartyId(thirdPartyId);
    }

    @Override
    public Boolean verifyTrade(String payerId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        BigDecimal zero=new BigDecimal(0);
        if (amount.compareTo(zero)==-1)
            return false;
        BigDecimal balance=findBalance(payerId);
        if (balance.compareTo(amount)==0 || balance.compareTo(amount)==1)
            return true;
        else
            return false;
    }

    @Override
    public Boolean verifyPassword(String thirdPartyId, String password) throws SQLException,IllegalArgumentException
    {
        if (password.equals(p2pAccountDao.findPasswordByThirdPartyId(thirdPartyId)))
            return true;
        else
            return false;
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
        String password=p2pAccountDao.findPasswordByThirdPartyId(thirdPartyId);
        if (password==null||password.equals(""))
            return false;
        else
            return true;
    }

    @Override
    public Boolean recharge(String thirdPartyId, String cardId, BigDecimal amount) throws SQLException,IllegalArgumentException
    {
        BigDecimal zero=new BigDecimal(0);
        if (amount.compareTo(zero)==-1)
            return false;
        BigDecimal cardBalance=bankAccountDao.findBalanceByCardId(cardId);
        if (cardBalance.compareTo(amount)==0 || cardBalance.compareTo(amount)==1)
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
        BigDecimal zero=new BigDecimal(0);
        if (amount.compareTo(zero)==-1)
            return false;
        BigDecimal p2pBalance=findBalance(thirdPartyId);
        if (p2pBalance.compareTo(amount)==0 || p2pBalance.compareTo(amount)==1)
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
