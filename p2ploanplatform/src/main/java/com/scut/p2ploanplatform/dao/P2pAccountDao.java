package com.scut.p2ploanplatform.dao;


import com.scut.p2ploanplatform.entity.P2pAccount;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
@Mapper
public interface P2pAccountDao {
    @Insert("insert into p2p_account(user_id, name, payment_password, balance, status, type) values (#{userId}, #{name}, #{paymentPassword}, #{balance}, #{status}, #{type})")
    int insertP2pAccount(P2pAccount p2pAccount);

    @Select("select balance from p2p_account where user_id = #{userId}")
    BigDecimal findBalanceByUserId(String userId);

    @Select("select * from p2p_account where user_id = #{userId}")
    List<P2pAccount> findByUserId(String userId);

    @Select("select * from p2p_account where user_id = #{userId} and status = #{status}")
    List<P2pAccount> findByUserIdAndStatus(String userId, int status);

    @Update("update p2p_account set balance = #{balance} where user_id = #{userId}")
    int updateBalance(String userId, BigDecimal balance);

    @Update("update p2p_account set status = #{status} where user_id = #{userId}")
    int updateStatus(String userId, int status);

    @Update("update p2p_account set name = #{name} where user_id = #{userId}")
    int updateName(String userId, String name);

    @Update("update p2p_account set payment_password = #{paymentPassword} where user_id = #{userId}")
    int updatePaymentPassword(String userId, String paymentPassword);
}
