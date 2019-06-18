package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.GrantCredit;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


/**
 * @author: Light
 * @date: 2019/6/17 20:11
 * @description:
 */

@Repository
@Mapper
public interface GrantCreditDao {

    @Insert("INSERT INTO `p2p`.`grant_credit` (`user_id`, `income`, `quota`, `rate`, `expire`) VALUES (#{userId}, #{income}, #{quota}, #{rate}, #{expire})")
    int insertGrantCredit(GrantCredit grantCredit);

    @Update("UPDATE `p2p`.`grant_credit` SET `user_id` = #{userId}, `income` = #{income}, `quota`= #{quota}, `rate` = #{rate}, `expire` = #{expire}")
    int updateGrantCredit(GrantCredit grantCredit);

    @Select("SELECT * FROM `p2p`.`grant_credit` WHERE `user_id` = #{userId}")
    GrantCredit selectGrantCredit(String userId);

    @Delete("DELETE FROM `p2p`.`grant_credit` WHERE `user_id` = #{userId}")
    int deleteGrantCredit(String userId);

}
