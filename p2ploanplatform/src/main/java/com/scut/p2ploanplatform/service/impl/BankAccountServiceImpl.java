package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.BankAccountDao;
import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountDao bankAccountDao;

    @Override
    public int addBankAccount(String cardId, String thirdPartyId, String paymentPassword, BigDecimal balance) throws SQLException,IllegalArgumentException
    {
        if (bankAccountDao.findCardByCardId(cardId)!= null)
            return 0;
        else
        {
            BankAccount bankAccount=new BankAccount();
            bankAccount.setCardID(cardId);
            bankAccount.setThirdPartyId(thirdPartyId);
            bankAccount.setPaymentPassword(paymentPassword);
            bankAccount.setBalance(balance);
            bankAccountDao.addBankAccount(bankAccount);
            return 1;
        }
    }

    @Override
    public BigDecimal findBalanceByCardId(String cardId) throws SQLException,IllegalArgumentException
    {
        return bankAccountDao.findBalanceByCardId(cardId);
    }

    @Override
    public BankAccount findCardByThirdPartyId(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        return bankAccountDao.findCardByThirdPartyId(thirdPartyId);
    }

    @Override
    public Boolean verifyPassword(String cardId,String paymentPassword) throws SQLException,IllegalArgumentException
    {
        String truePassword=bankAccountDao.findPaymentPasswordByCardId(cardId);
        if (truePassword.equals(paymentPassword))
            return true;
        else
            return false;
    }
}
