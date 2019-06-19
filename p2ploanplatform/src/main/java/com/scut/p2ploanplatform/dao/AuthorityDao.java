package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.Authority;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author: Light
 * @date: 2019/6/19 10:52
 * @description:
 */
public interface AuthorityDao {

    @Insert("INSERT INTO `p2p`.`authority` (`authority_id`, `authority_amount`) VALUES (#{authorityId}, #{authorityAmount}")
    int insertAuthority(Authority authority);

    @Update("UPDATE `p2p`.`authority` SET `authority_amount` = #{authorityAmount} WHERE `authority_id` = #{authorityId}")
    int updateAuthority(Authority authority);

    @Select("SELECT * FROM `p2p`.`authority` WHERE authority_id = #{authorityId}")
    List<Authority> selectAllAuthority(String authorityId);

    @Delete("DELETE FROM `p2p`.`authority` WHERE authority_id = #{authorityId}")
    int deleteAuthority(String authorityId);

}
