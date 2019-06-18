package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.entity.Purchase;

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
    Boolean createPurchaseItem(String investorId, Integer applicationId);

    /**
     * 订单逾期（修改订单状态）
     * @param purchaseId 订单id
     * @return 操作状态（成功/失败）
     */
    Boolean purchaseOverdue(Integer purchaseId);

    /**
     * 查询所有订单
     * @return 订单列表
     */
    List<Purchase> showAllPurchase();

    /**
     * 根据订单id查询订单
     * @param purchaseId 订单id
     * @return 订单列表
     */
    Purchase showPurchaseById(Integer purchaseId);

    /**
     * 查询特定投资人的所有订单
     * @param investorID 投资人Id
     * @return 订单列表
     */
    List<Purchase> showPurchaseByInvestorId(String investorID);

    /**
     * 完成订单
     * @param purchaseId 订单Id
     * @return 操作状态（成功/失败)
     */
    Boolean accomplishPurchase(Integer purchaseId);
}
