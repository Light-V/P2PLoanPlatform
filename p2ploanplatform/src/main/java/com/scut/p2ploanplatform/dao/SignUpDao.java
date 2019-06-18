package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface SignUpDao {

    @Insert("INSERT INTO `p2p`.`User`(`user_id`, `department_id`, `password`, `phone`, `id_card`, `third_party_id`, `name`,`address`)" +
            " VALUES (#{userId}, #{departmentId}, #{password}, #{phone}, #{idCard}, #{thirdPartyId}, #{name}, #{address})")
    int insertUser(User newUser);

    @Select("SELECT * FROM `p2p`.`User` WHERE `user_id` = #{value}")
    User findUser(String id);

}
