package com.scut.p2ploanplatform.entity;

import com.scut.p2ploanplatform.enums.LoanStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 订单类
 *
 * 包含订单的详细信息
 * @author FatCat
 */
@Data
public class Purchase {
    /**
     * 订单Id(自增)
     */
    private Integer purchaseId;
    /**
     * 借款申请Id（自增）
     */
    private Integer applicationId;
    /**
     * 借款人Id
     */
    private String borrowerId;
    /**
     * 担保人Id
     */
    private String guarantorId;
    /**
     * 投资人Id
     */
    private String investorId;
    /**
     * 借款申请标题
     */
    private String title;
    /**
     * 购买时间
     */
    private Date purchaseTime;
    /**
     * 订单状态
     * 合约中，逾期
     */
    private Integer status;
    /**
     * 借款金额
     */
    private BigDecimal amount;
    /**
     * 月利率
     */
    private BigDecimal interestRate;
    /**
     * 借款时长
     */
    private Integer loanMonth;
    /**
     * 创建时间（申请提交时间）
     */
    private Date createTime;
    /**
     * 修改时间（最后一次状态改变时间）
     */
    private Date updateTime;
    /**
     * 还款计划
     */
    private List<RepayPlan> repayPlans;

    public Purchase(LoanApplication application) {
        this.applicationId = application.getApplicationId();
        this.borrowerId = application.getBorrowerId();
        this.guarantorId = application.getGuarantorId();
        this.title = application.getTitle();
        this.purchaseTime = Calendar.getInstance().getTime();
        this.status = LoanStatus.SUBSCRIBED.getStatus();
        this.amount = application.getAmount();
        this.interestRate = application.getInterestRate();
        this.loanMonth = application.getLoanMonth();
    }

    public Purchase(){

    }
}
