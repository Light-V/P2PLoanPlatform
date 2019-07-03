package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public interface RepayService {
    /**
     * 添加还款计划
     * @param purchaseId 认购ID
     * @param repayDate 还款日期
     * @param amount 还款金额
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 添加成功后返回还款计划
     */
    RepayPlan insertPlan(Integer purchaseId, Date repayDate, BigDecimal amount) throws SQLException, IllegalArgumentException;

    /**
     * 根据认购ID查询所有还款计划信息
     * @param id 还款ID
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException ID为空
     * @return 与该认购ID有关的所有还款信息，无记录时返回空List
     */
    List<RepayPlan> findPlanByPurchaseId(Integer id) throws SQLException, IllegalArgumentException;

    /**
     * 根据计划ID查询该还款计划信息
     * @param id 计划ID
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException ID为空
     * @return 与该计划ID有关的还款信息，无记录时返回null
     */
    RepayPlan findPlanById(String id) throws SQLException, IllegalArgumentException;

    /**
     * 更新还款计划信息
     * @param id 计划ID
     * @param status 还款计划状态
     * @param realRepayDate 实际还款日期（为null则为未还）
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     */
    void updateRepayPlan(String id, RepayPlanStatus status, Date realRepayDate) throws SQLException, IllegalArgumentException;

    /**
     * 根据认购ID查询该ID下所有的还款计划是否已完成还款
     * @param purchaseId 认购ID
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 还款计划的状态，查询不到该认购ID则返回null
     */
    Boolean isRepayCompleted(Integer purchaseId) throws SQLException, IllegalArgumentException;

    /**
     * 查询认购ID下所有还款计划的最大逾期天数
     * @param purchaseId 认购ID
     * @return 若无该ID则返回空，否则返回逾期天数（无逾期则返回0）
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     */
    Integer getRepayOverdueDay(Integer purchaseId) throws SQLException, IllegalArgumentException;
}
