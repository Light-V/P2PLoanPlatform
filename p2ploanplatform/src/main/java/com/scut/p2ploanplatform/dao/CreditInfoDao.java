package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.CreditInfo;
import com.scut.p2ploanplatform.form.CreditInfoForm;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * @author: Light
 * @date: 2019/6/17 19:51
 * @description:
 */

@Repository
@Mapper
public interface CreditInfoDao {

    @Insert("INSERT INTO `p2p`.`credit_info` (`user_id`, `income`, `family_income`, `assets`, `family_number`, `debt`, `credit_score`) VALUES (#{userId}, #{income}, #{familyIncome}, #{assets}, #{familyNumber}, #{debt}, #{creditScore})")
    int insertCreditInfo(CreditInfo creditInfo);

    @Update("UPDATE `p2p`.`credit_info` SET `income` = #{income}, `family_income` = #{familyIncome}, `assets` = #{assets}, `family_number` = #{familyNumber}, `debt` = #{debt} WHERE `user_id` = #{userId}")
    int updateCreditInfo(CreditInfo creditInfo);

    @Update("UPDATE `p2p`.`credit_info` SET `income` = #{income}, `family_income` = #{familyIncome}, `assets` = #{assets}, `family_number` = #{familyNumber}, `debt` = #{debt}, `credit_score` = #{creditScore} WHERE `user_id` = #{userId}")
    int updateAllCreditInfo(CreditInfo creditInfo);

    @Select("SELECT * FROM `p2p`.`credit_info` WHERE user_id = #{userId}")
    CreditInfo selectCreditInfo(String userId);

    @Delete("DELETE FROM `p2p`.`credit_info` WHERE user_id = #{userId}")
    int deleteCreditInfo(String userId);

    @Select("SELECT `credit_score` FROM `p2p`.`credit_info` WHERE user_id = #{userId}")
    int selectCreditScore(String userId);

    @Update("UPDATE `p2p`.`credit_info` SET `credit_score` = #{creditScore} WHERE `user_id` = #{userId}")
    int updateCreditScore(String userId, Integer creditScore);

}
