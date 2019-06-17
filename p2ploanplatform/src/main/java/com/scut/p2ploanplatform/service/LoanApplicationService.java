package com.scut.p2ploanplatform.service;

import org.springframework.stereotype.Service;
import com.scut.p2ploanplatform.entity.LoanApplication;

import java.sql.SQLException;
import java.util.List;

/**
 * @author FatCat
 */

public interface LoanApplicationService {
    /**
     * 添加借款申请
     * @param loanApplication 借款申请
     * @return 操作状态（成功/失败)
     * @throws SQLException sql错误
     */
    Integer addApplication(LoanApplication loanApplication) throws SQLException;

    /**
     * 根据借款申请id修改借款申请状态
     * @param id     借款申请id
     * @param status 借款申请状态值
     * @return 操作状态（成功/失败)
     * @throws SQLException sql错误
     * @throws IllegalArgumentException 非法参数错误
     */
    Integer changeStatusById(Integer id,Integer status) throws SQLException,IllegalArgumentException;

    /**
     * 删除借款申请
     * 管理员操作，不允许外部调用
     * @param id 借款申请Id
     * @throws SQLException sql错误
     */
    void deleteApplicationById(Integer id) throws SQLException;

    /**
     * 查询特定借款人的所有借款申请
     * @param borrowerId 借款人Id
     * @return 借款申请列表
     * @throws SQLException sql错误
     * @throws IllegalArgumentException 非法参数错误
     */
    List<LoanApplication> showApplicationByBorrowerId(String borrowerId) throws  SQLException,IllegalArgumentException;

    /**
     * 查询特定担保人担保的所有借款申请
     * @param guarantorId 担保人Id
     * @return 借款申请列表
     * @throws SQLException sql错误
     * @throws IllegalArgumentException 非法参数错误
     */
    List<LoanApplication> showApplicationByGuarantorId(String guarantorId) throws SQLException,IllegalArgumentException;

    /**
     * 查询特定借款人特定状态的所有借款申请
     * @param borrowerId 借款人Id
     * @param status 借款申请状态
     * @return 借款申请列表
     * @throws SQLException sql错误
     * @throws IllegalArgumentException 非法参数错误
     */
    List<LoanApplication> showApplicationByBorrowerId(String borrowerId, Integer status) throws  SQLException,IllegalArgumentException;

    /**
     * 查询特定担保人担保的特定状态的借款申请
     * @param guarantorId 担保人Id
     * @param status 借款申请状态
     * @return 借款申请列表
     * @throws SQLException sql错误
     * @throws IllegalArgumentException 非法参数错误
     */
    List<LoanApplication> showApplicationByGuarantorId(String guarantorId, Integer status) throws SQLException,IllegalArgumentException;

    /**
     * 查询所有审核已通过的借款申请
     * 产品交易平台展示内容
     * @return 借款申请列表
     * @throws SQLException sql错误
     * @throws IllegalArgumentException 非法参数错误
     */
    List<LoanApplication> showApplicationReviewedPassed() throws SQLException,IllegalArgumentException;
}
