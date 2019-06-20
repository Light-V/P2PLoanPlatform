package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.BankAccount;
import com.scut.p2ploanplatform.entity.P2pAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Mapper
@Repository
public interface BankAccountDao {
    @Insert("insert into bank_account(card_id, user_id, name, payment_password, balance) values (#{userId}, #{name}, #{paymentPassword}, #{balance})")
    int insertBankAccount(BankAccount bankAccount);

    @Select("select balance from bank_account where user_id = #{userId}")
    BigDecimal findBalanceByUserId(String userId);

    @Update("update bank_account set balance = #{balance} where user_id = #{userId}")
    int updateBalance(String userId, BigDecimal balance);

}
