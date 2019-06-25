package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.BankAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
@Mapper
public interface BankAccountDao {
    @Insert("insert into `p2p`.`bank_account`(`card_id`, `third_party_id`, `payment_password`, `balance`) values (#{cardId}, #{thirdPartyId}, #{paymentPassword}, #{balance})")
    int addBankAccount(BankAccount bankAccount);

    @Select("select * from `p2p`.`bank_account` where `card_id` = #{cardId}")
    BankAccount findCardByCardId(String cardId);

    @Select("select `balance` from `p2p`.`bank_account` where `card_id` = #{cardId}")
    BigDecimal findBalanceByCardId(String cardId);

    @Select("select `payment_password` from `p2p`.`bank_account` where `cardId` = #{cardId}")
    String findPaymentPasswordByCardId(String cardId);

    @Select("select * from `p2p`.`bank_account` where `third_party_id`=#{thirdPartyId}")
    BankAccount findCardByThirdPartyId(String thirdPartyId);

    @Update("update `p2p`.`bank_account` set `balance` = #{balance} where `card_id` = #{cardId}")
    int updateBalance(String cardId, BigDecimal balance);

}
