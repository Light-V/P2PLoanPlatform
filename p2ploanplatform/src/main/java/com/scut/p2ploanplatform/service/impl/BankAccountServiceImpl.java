package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.BankAccountDao;
import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.service.BankAccountService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    @Autowired
    BankAccountDao bankAccountDao;

    @Override
    public int addBankAccount(String cardId, String userId, String name, String paymentPassword, BigDecimal balance) throws SQLException,IllegalArgumentException
    {
        if (bankAccountDao.findCardByCardId(cardId)!= null)
            return 0;
        else
        {
            BankAccount bankAccount=new BankAccount();
            bankAccount.setCardID(cardId);
            bankAccount.setUserID(userId);
            bankAccount.setName(name);
            bankAccount.setPaymentPassword(paymentPassword);
            bankAccount.setBalance(balance);
            bankAccountDao.insertBankAccount(bankAccount);
            return 1;
        }
    }

    @Override
    public BigDecimal showBalanceByCardId(String cardId) throws SQLException,IllegalArgumentException
    {
        return bankAccountDao.findBalanceByCardId(cardId);
    }

    @Override
    public List<BankAccount> showCardsByUserId(String userId) throws SQLException,IllegalArgumentException
    {
        return bankAccountDao.findCardsByUserId(userId);
    }
}
