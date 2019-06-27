package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.BankAccountDao;
import com.scut.p2ploanplatform.dao.P2pAccountDao;
import com.scut.p2ploanplatform.entity.P2pAccount;
import com.scut.p2ploanplatform.service.P2pAccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
@Service
public class P2pAccountServiceImpl implements P2pAccountService {

    @Autowired
    private P2pAccountDao p2pAccountDao;

    @Autowired
    private BankAccountDao bankAccountDao;

    private String apiKey;

    public String generateApiKey()
    {
        String apiKey=RandomStringUtils.randomAlphanumeric(12);
        this.apiKey=apiKey;
        return apiKey;
    }

    public String getApiKey()
    {
        return apiKey;
    }

    @Override
    public int addP2pAccount(String thirdPartyId, String paymentPassword, BigDecimal balance, Integer status, Integer type) throws SQLException,IllegalArgumentException
    {
        if (balance.compareTo(BigDecimal.ZERO)<0||(status!=1&&status!=0)||(type!=1&&type!=0))
            throw new IllegalArgumentException("非法参数！");
        if (verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为 %s 的账户已经存在！",thirdPartyId));
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
    public int updatePassword(String thirdPartyId, String paymentPassword) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为 %s 的账户还未创建！",thirdPartyId));
        try
        {
            p2pAccountDao.updatePaymentPassword(thirdPartyId,paymentPassword);
            return 1;
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    @Override
    public BigDecimal findBalance(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为 %s 的账户还未创建！",thirdPartyId));
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
            throw new IllegalArgumentException(String.format("id为 %s 的账户还未创建！",payerId));
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
            throw new IllegalArgumentException(String.format("id为 %s 的账户还未创建！",thirdPartyId));
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
            throw new IllegalArgumentException(String.format("id为 %s 的账户还未创建！",thirdPartyId));
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

    @Override
    public int freeze(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为%s的账户还未创建！",thirdPartyId));
        try
        {
            p2pAccountDao.updateStatus(thirdPartyId,0);
            return 1;
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    @Override
    public int unfreeze(String thirdPartyId) throws SQLException,IllegalArgumentException
    {
        if (!verifyIfExists(thirdPartyId))
            throw new IllegalArgumentException(String.format("id为%s的账户还未创建！",thirdPartyId));
        try
        {
            p2pAccountDao.updateStatus(thirdPartyId,1);
            return 1;
        }
        catch (Exception e)
        {
            throw new SQLException(e);
        }
    }

    public String getSHA(String input)
    {

        try {

            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);

            return null;
        }
    }
}
