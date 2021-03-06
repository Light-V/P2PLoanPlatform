package com.scut.p2ploanplatform.service;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.exception.LoanStatusException;

import java.sql.SQLException;
import java.util.List;

/**
 * @author FatCat
 */
public interface PurchaseService {
    /**
     * 新建订单信息（产品撮合成功）
     * @param investorId 投资人Id
     * @param applicationId 投资人投资的借款申请Id
     * @return 操作状态（成功/失败)
     */
    Purchase subscribed(String investorId, Integer applicationId, String password) throws Exception;

    /**
     * 订单逾期（修改订单状态）
     * @param purchaseId 订单id
     * @return 操作状态（成功/失败）
     */
    Boolean purchaseOverdue(Integer purchaseId) throws SQLException,IllegalArgumentException;

    /**
     * 订单逾期（修改订单状态）
     * @param purchaseId 订单id
     * @return 操作状态（成功/失败）
     */
    Boolean overdueToSubscribed(Integer purchaseId) throws SQLException, IllegalArgumentException;
    /**
     * 完成订单
     * @param purchaseId 订单Id
     * @return 操作状态（成功/失败)
     */
    Boolean accomplishPurchase(Integer purchaseId) throws SQLException,IllegalArgumentException;

    /**
     * 根据订单id查询订单
     * @param purchaseId 订单id
     * @return 订单列表
     */
    Purchase showPurchaseById(Integer purchaseId) throws SQLException,IllegalArgumentException;

    /**
     * 根据订单id查询订单
     * @param applicationId 借款申请id
     * @return 订单列表
     */
    Purchase showPurchaseByApplicationId(Integer applicationId) throws SQLException,IllegalArgumentException;

    /**
     * 查询所有订单
     * @return 订单列表
     */
    PageInfo<Purchase> showAllPurchase(Integer pageNum, Integer pageSize) throws SQLException;

    /**
     * 查询特定投资人的所有订单
     * @param investorID 投资人Id
     * @return 订单列表
     */
    PageInfo<Purchase> showPurchaseByInvestorId(String investorID, Integer pageNum, Integer pageSize) throws SQLException,IllegalArgumentException;

    /**
     * 查询特定投资人特定状态的订单
     * @param investorID 投资人Id
     * @param status 订单状态
     * @return 订单列表
     */
    PageInfo<Purchase> showPurchaseByInvestorId(String investorID, LoanStatus status, Integer pageNum, Integer pageSize) throws SQLException,IllegalArgumentException;

    /**
     * 查询特定借款人的所有订单
     * @param borrowerID 借款人ID
     * @return 订单列表
     */
    PageInfo<Purchase> showPurchaseByBorrowerId(String borrowerID, Integer pageNum, Integer pageSize) throws SQLException,IllegalArgumentException;

    /**
     * 查询特定借款人特定状态的订单
     * @param borrowerID 借款人ID
     * @param status 订单状态
     * @return 订单列表
     */
    PageInfo<Purchase> showPurchaseByBorrowerId(String borrowerID, LoanStatus status, Integer pageNum, Integer pageSize) throws SQLException,IllegalArgumentException;

    /**
     * 查询所有审核通过逾期的申请
     * 产品交易平台展示内容
     * @param pageNum 请求页码
     * @param pageSize 每页包含的字段数
     * @param userId 审核人Id
     * @return 借款申请列表
     * @throws SQLException sql错误
     * @throws IllegalArgumentException 非法参数错误
     */
    PageInfo<Purchase> getOverdueApplicationById(Integer pageNum, Integer pageSize, String userId) throws SQLException;

}
