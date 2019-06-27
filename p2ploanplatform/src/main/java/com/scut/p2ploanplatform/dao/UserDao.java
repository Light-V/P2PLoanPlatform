package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
/**
 * @auther: zrh
 * @date: 2019/6/17 2:05
 * @description:
 */


@Repository
@Mapper
public interface UserDao {

    @Insert("INSERT INTO `p2p`.`user`(`user_id`, `department_id`, `password`, `phone`, `id_card`, `third_party_id`, `name`,`address`)" +
            " VALUES (#{userId}, #{departmentId}, #{password}, #{phone}, #{idCard}, #{thirdPartyId}, #{name}, #{address})")
    int insertUser(User newUser);

    @Select("SELECT * FROM `p2p`.`user` WHERE `user_id` = #{value}")
    User findUser(String id);

    @Update("UPDATE `p2p`.`user` SET `password` = #{password} WHERE `user_id` = #{userId}")
    int updatePassword(User user);

    @Update("UPDATE `p2p`.`user` SET `phone` = #{phone}, `address` = #{address} WHERE `user_id` = #{userId}")
    int updateUser(User user);

    @Delete("DELETE FROM `p2p`.`user` WHERE `user_id` = #{value}")
    int deleteUser(String id);
}
