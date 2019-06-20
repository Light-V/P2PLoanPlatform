package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.LoanStatus;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
@Repository
@Mapper
public interface LoanApplicationDao {
    /**
     * 添加借款申请
     * @param loanApplication 借款申请
     * @return 操作状态（成功/失败)
     */
    @Insert("INSERT INTO `loan_application` (`borrower_id`, `guarantor_id`, `title`, `status`, `amount`, `interest_rate`, `loan_month`, `purchase_deadline`)\n" +
            "VALUES (#{borrowerId}, #{guarantorId},#{title}, #{status}, #{amount}, #{interestRate},#{loanMonth}, #{purchaseDeadline})")
    @Options(useGeneratedKeys = true, keyProperty = "applicationId", keyColumn = "application_id")
    Integer addApplication(LoanApplication loanApplication);

    /**
     * 根据借款申请id修改借款申请状态
     * @param id     借款申请id
     * @param loanStatus 借款申请状态
     * @return 操作状态（成功/失败)
     */
    @Update("UPDATE `loan_application` SET `status`= #{loanStatus} WHERE `application_Id`=#{id} ")
    Integer changeStatusById(Integer id, Integer loanStatus);

    /**
     * 根据借款申请id修改借款申请信息
     * @param loanApplication     借款申请id
     * @return 操作状态（成功/失败)
     */
//    @Update("UPDATE `loan_application` SET `application_Id ")
    Integer modifyApplication(LoanApplication loanApplication);

    /**
     * 删除借款申请
     * 管理员操作，不允许外部调用
     * @param id 借款申请Id
     */
    @Delete("DELETE FROM `loan_application` WHERE `application_id` = #{id}")
    Integer deleteApplicationById(Integer id);

    /**
     * 根据借款申请ID查询借款申请
     * @param ApplicationId 借款申请Id
     * @return 借款申请
     */
    @Select("SELECT * FROM `loan_application` WHERE `application_id`= #{ApplicationId}")
    LoanApplication showApplicationByApplicationId(Integer ApplicationId);

    /**
     * 查询特定借款人的所有借款申请
     * @param borrowerId 借款人Id
     * @return 借款申请列表
     */
    @Select("SELECT * FROM `loan_application` WHERE `borrower_id`= #{borrowerId}")
    List<LoanApplication> showApplicationByBorrowerId(String borrowerId);

    /**
     * 查询特定担保人担保的所有借款申请
     * @param guarantorId 担保人Id
     * @return 借款申请列表
     */
    @Select("SELECT * FROM `loan_application` WHERE `guarantor_id`= #{guarantorId}")
    List<LoanApplication> showApplicationByGuarantorId(String guarantorId);

    /**
     * 查询特定借款人特定状态的所有借款申请
     * @param borrowerId 借款人Id
     * @param loanStatus 借款申请状态
     * @return 借款申请列表
     */
    @Select("SELECT * FROM `loan_application` WHERE `borrower_id`= #{borrowerId} AND `status` = #{loanStatus}")
    List<LoanApplication> showApplicationByBorrowerIdAndStatus(String borrowerId, Integer loanStatus);

    /**
     * 查询特定担保人担保的特定状态的借款申请
     * @param guarantorId 担保人Id
     * @param loanStatus 借款申请状态
     * @return 借款申请列表
     */
    @Select("SELECT * FROM `loan_application` WHERE `guarantor_id`= #{guarantorId} AND `status` = #{loanStatus}")
    List<LoanApplication> showApplicationByGuarantorIdAndStatus(String guarantorId, Integer loanStatus);

    /**
     * 查询所有审核已通过的借款申请
     * 产品交易平台展示内容
     * @return 借款申请列表
     */
    @Select("SELECT * FROM `loan_application` WHERE `status` = 1")
    List<LoanApplication> showApplicationReviewedPassed();
}