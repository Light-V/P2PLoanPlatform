package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.BankAccountDao;
import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.service.BankAccountService;
import com.scut.p2ploanplatform.service.P2pAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountDao bankAccountDao;

    @Autowired
    P2pAccountService p2pAccountService;

    @Override
    public int addBankAccount(String cardId, String thirdPartyId, String paymentPassword, BigDecimal balance) throws SQLException,IllegalArgumentException
    {
        if (bankAccountDao.findCardByCardId(cardId)!= null)
            throw new IllegalArgumentException(String.format("卡号为%s的银行卡已被添加！",cardId));
        else
        {
            try
            {
                BankAccount bankAccount=new BankAccount();
                bankAccount.setCardID(cardId);
                bankAccount.setThirdPartyId(thirdPartyId);
                bankAccount.setPaymentPassword(paymentPassword);
                bankAccount.setBalance(balance);
                bankAccountDao.addBankAccount(bankAccount);
                return 1;
            }
            catch (Exception e)
            {
                throw new SQLException(e);
            }
        }
    }

    @Override
    public BigDecimal findBalanceByCardId(String cardId) throws SQLException,IllegalArgumentException
    {
        if (bankAccountDao.findCardByCardId(cardId)== null)
            throw new IllegalArgumentException(String.format("卡号为%s的银行卡还未添加！",cardId));
        else
        {
            try
            {
                return bankAccountDao.findBalanceByCardId(cardId);
            }
            catch (Exception e)
            {
                throw new SQLException(e);
            }
        }

    }

    @Override
    public BankAccount findCardByThirdPartyId(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        try
        {
            return bankAccountDao.findCardByThirdPartyId(thirdPartyId);
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    @Override
    public Boolean verifyCardIsAdded(String cardId) throws SQLException,IllegalArgumentException
    {
        if (bankAccountDao.findCardByCardId(cardId)==null)
            return false;
        else
            return true;
    }

    @Override
    public Boolean verifyPassword(String cardId,String paymentPassword) throws SQLException,IllegalArgumentException
    {
        if (bankAccountDao.findCardByCardId(cardId)== null)
            throw new IllegalArgumentException(String.format("卡号为%s的银行卡还未添加！",cardId));
        try
        {
            String truePassword=bankAccountDao.findPaymentPasswordByCardId(cardId);
            return truePassword.equals(paymentPassword);
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }
}
