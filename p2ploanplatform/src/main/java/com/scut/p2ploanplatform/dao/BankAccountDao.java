package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.BankAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Mapper
@Repository
public interface BankAccountDao {
    @Insert("insert into bank_account(card_id, user_id, name, payment_password, balance) values (#{userId}, #{name}, #{paymentPassword}, #{balance})")
    int insertBankAccount(BankAccount bankAccount);

    @Select("select balance from bank_account where card_id = #{cardId}")
    BigDecimal findBalanceByCardId(String cardId);

    @Select("select * from bank_account where user_id=#{userId}")
    List<BankAccount> findCardsByUserId(String userId);

    @Update("update bank_account set balance = #{balance} where card_id = #{cardId}")
    int updateBalance(String cardId, BigDecimal balance);

}
