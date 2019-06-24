package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 借款申请类
 *
 * 包含借款申请的详细信息
 * @author FatCat
 */
@Data
public class LoanApplication {
    /**
     * 产品Id(自增)
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
     * 借款申请标题
     */
    private String title;
    /**
     * 产品状态
     * 详情见LoanStatus枚举类
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
     * 认购期限
     */
    private Date purchaseDeadline;
    /**
     * 创建时间（申请提交时间）
     */
    private Date createTime;
    /**
     * 修改时间（最后一次状态改变时间）
     */
    private Date updateTime;

    @Override
    public String toString() {
        return "{" +
                "  applicationId=" + applicationId +
                ", borrowerId='" + borrowerId + '\'' +
                ", guarantorId='" + guarantorId + '\'' +
                ", status=" + status +
                ", amount=" + amount +
                ", interestRate=" + interestRate +
                ", loanMonth=" + loanMonth +
                ", purchaseDeadline=" + purchaseDeadline +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
