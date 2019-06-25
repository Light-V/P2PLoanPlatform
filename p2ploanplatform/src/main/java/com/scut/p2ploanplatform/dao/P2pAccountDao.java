package com.scut.p2ploanplatform.dao;


import com.scut.p2ploanplatform.entity.P2pAccount;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
@Mapper
public interface P2pAccountDao {
    @Insert("insert into `p2p`.`p2p_account`(`third_party_id`, `payment_password`, `balance`, `status`, `type`) values (#{thirdPartyId}, #{paymentPassword}, #{balance}, #{status}, #{type})")
    int addP2pAccount(P2pAccount p2pAccount);

    @Select("select balance from `p2p`.`p2p_account` where `third_party_id` = #{thirdPartyId}")
    BigDecimal findBalanceByThirdPartyId(String thirdPartyId);

    @Select("select * from `p2p`.`p2p_account` where `third_party_id` = #{thirdPartyId}")
    P2pAccount findByThirdPartyId(String thirdPartyId);

    @Select("select `payment_password` from `p2p`.`p2p_account` where `third_party_id` = #{thirdPartyId}")
    String findPasswordByThirdPartyId(String thirdPartyId);

    @Select("select * from `p2p`.`p2p_account` where `status` = #{status}")
    List<P2pAccount> findByStatus(Integer status);

    @Update("update `p2p`.`p2p_account` set `balance` = #{balance} where `third_party_id` = #{thirdPartyId}")
    int updateBalance(String thirdPartyId, BigDecimal balance);

    @Update("update `p2p`.`p2p_account` set `status` = #{status} where `third_party_id` = #{thirdPartyId}")
    int updateStatus(String thirdPartyId, Integer status);

    @Update("update `p2p`.`p2p_account` set `payment_password` = #{paymentPassword} where `third_party_id` = #{thirdPartyId}")
    int updatePaymentPassword(String thirdPartyId, String paymentPassword);
}
