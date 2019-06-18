package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.LoanStatus;
import java.sql.SQLException;
import java.util.List;

public interface LoanApplicationDao {
    /**
     * 添加借款申请
     * @param loanApplication 借款申请
     * @return 操作状态（成功/失败)
     */
    Boolean addApplication(LoanApplication loanApplication);

    /**
     * 根据借款申请id修改借款申请状态
     * @param id     借款申请id
     * @param loanStatus 借款申请状态
     * @return 操作状态（成功/失败)
     */
    Boolean changeStatusById(Integer id, LoanStatus loanStatus);

    /**
     * 根据借款申请id修改借款申请信息
     * @param loanApplication     借款申请id
     * @return 操作状态（成功/失败)
     */
    Boolean modifyApplication(LoanApplication loanApplication);

    /**
     * 删除借款申请
     * 管理员操作，不允许外部调用
     * @param id 借款申请Id
     */
    Boolean deleteApplicationById(Integer id);

    /**
     * 查询特定借款人的所有借款申请
     * @param borrowerId 借款人Id
     * @return 借款申请列表
     */
    List<LoanApplication> showApplicationByBorrowerId(String borrowerId);

    /**
     * 查询特定担保人担保的所有借款申请
     * @param guarantorId 担保人Id
     * @return 借款申请列表
     */
    List<LoanApplication> showApplicationByGuarantorId(String guarantorId);

    /**
     * 查询特定借款人特定状态的所有借款申请
     * @param borrowerId 借款人Id
     * @param loanStatus 借款申请状态
     * @return 借款申请列表
     */
    List<LoanApplication> showApplicationByBorrowerId(String borrowerId, LoanStatus loanStatus);

    /**
     * 查询特定担保人担保的特定状态的借款申请
     * @param guarantorId 担保人Id
     * @param loanStatus 借款申请状态
     * @return 借款申请列表
     */
    List<LoanApplication> showApplicationByGuarantorId(String guarantorId, LoanStatus loanStatus);

    /**
     * 查询所有审核已通过的借款申请
     * 产品交易平台展示内容
     * @return 借款申请列表
     */
    List<LoanApplication> showApplicationReviewedPassed();
}
