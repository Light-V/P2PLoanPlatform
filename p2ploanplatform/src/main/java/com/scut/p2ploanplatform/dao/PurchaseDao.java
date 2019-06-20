package com.scut.p2ploanplatform.dao;

import java.util.List;

public interface Purchase {
    /**
     * 新建订单信息（产品撮合成功）
     * @param investorId 投资人Id
     * @param applicationId 投资人投资的借款申请Id
     * @return 操作状态（成功/失败)
     */
    Boolean createPurchaseItem(String investorId, Integer applicationId);

    /**
     * 修改订单信息状态(逾期时使用）
     * @param purchaseId 订单id
     * @return 操作状态（成功/失败）
     */
    Boolean changePurchaseItemStatus(Integer purchaseId);

    /**
     * 查询所有订单
     * @return 订单列表
     */
    List<com.scut.p2ploanplatform.entity.Purchase> showAllPurchase();

    /**
     * 查询特定投资人的所有订单
     * @param investorID 投资人Id
     * @return 订单列表
     */
    List<com.scut.p2ploanplatform.entity.Purchase> showoPurchaseByInvestorId(String investorID);

    /**
     * 完成订单
     * @param purchaseId 订单Id
     * @return 操作状态（成功/失败)
     */
    Boolean accomplishPushchase(Integer purchaseId);
}
