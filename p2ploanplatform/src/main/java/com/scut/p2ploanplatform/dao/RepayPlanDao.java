package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.RepayPlan;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface RepayPlanDao {

    @Insert("INSERT INTO `p2p`.`repay_plan`(`plan_id`, `purchase_id`, `repay_date`, `real_repay_date`, `amount`, `status`) VALUES (#{planId}, #{purchaseId}, #{repayDate}, #{realRepayDate}, #{amount}, #{status})")
    int insertPlan(RepayPlan plan);

    @Select("SELECT * FROM `p2p`.`repay_plan` WHERE `purchase_id` = #{value}")
    List<RepayPlan> findPlanByPurchaseId(Integer id);

    @Select("SELECT * FROM `p2p`.`repay_plan` WHERE `plan_id` = #{value}")
    RepayPlan findPlanByPlanId(String id);

    @Select("(SELECT * FROM `p2p`.`repay_plan` WHERE `repay_date` = CURRENT_DATE AND `status` = 0) UNION (SELECT * FROM `p2p`.`repay_plan` WHERE `status` = 2)")
    List<RepayPlan> findAllUnpaidPlan();

    @Update("UPDATE `p2p`.`repay_plan` SET `repay_date` = #{repayDate}, `real_repay_date` = #{realRepayDate}, `status` = #{status} WHERE `plan_id` = #{planId}")
    int updatePlan(RepayPlan plan);

    @Delete("DELETE FROM `p2p`.`repay_plan` WHERE `plan_id` = #{value}")
    int deletePlan(String id);

    @Update("UPDATE `p2p`.`repay_plan` SET `status` = 2 WHERE `repay_date` < CURRENT_DATE AND `status` = 0")
    int updatePlanStatus();
}
