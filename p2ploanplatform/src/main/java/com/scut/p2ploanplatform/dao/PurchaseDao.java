package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.Purchase;
//import javafx.application.Application;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Mapper
public interface PurchaseDao {
    /**
     * 新建订单信息（产品撮合成功）
     * @param purchase 订单
     * @return 操作状态（成功/失败)
     */
    @Insert("INSERT INTO `purchase` (`application_id`, `borrower_id`, `guarantor_id`,`investor_id`, `title`, `purchase_time`, `status`,  `amount`, `interest_rate`, `loan_month`)\n" +
            "VALUES (#{applicationId}, #{borrowerId}, #{guarantorId}, #{investorId}, #{title}, #{purchaseTime}, #{status}, #{amount}, #{interestRate},#{loanMonth})")
    @Options(useGeneratedKeys = true, keyProperty = "purchaseId", keyColumn = "purchase_id")
    Boolean createPurchaseItem(Purchase purchase);

    /**
     * 修改订单状态
     * @param purchaseID 订单Id
     * @param status 订单状态
     * @return 操作状态（成功/失败）
     */
    @Update("UPDATE `purchase` SET `status`= #{status} WHERE `purchase_id` = #{purchaseId}")
    Boolean updatePurchaseStatus(Integer purchaseID,Integer status);

    /**
     * 查询所有订单
     * @return 订单列表
     */
    @Select("SELECT * FROM `purchase`")
    List<Purchase> showAllPurchase();

    /**
     * 根据订单ID查询订单
     * @param purchaseId 订单Id
     * @return 订单列表
     */
    @Select("SELECT * FROM `purchase` WHERE `purchase_id` = #{purchaseId}")
    Purchase getPurchaseByPurchaseId(Integer purchaseId);

    /**
     * 查询特定投资人的所有订单
     * @param investorId 投资人Id
     * @return 订单列表
     */
    @Select("SELECT * FROM `purchase` WHERE `investor_id` = #{investorId}")
    List<Purchase> getPurchaseByInvestorId(String investorId);

    /**
     * 查询特定投资人的所有订单
     * @param borrowerId 投资人Id
     * @return 订单列表
     */
    @Select("SELECT * FROM `purchase` WHERE `borrower_id` = #{borrowerId}")
    List<Purchase> getPurchaseByBorrowerId(String borrowerId);
}
