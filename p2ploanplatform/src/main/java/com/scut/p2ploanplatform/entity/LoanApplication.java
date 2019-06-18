package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.sql.Date;

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
    private Integer productId;
    /**
     * 借款人Id
     */
    private String borrowerId;
    /**
     * 担保人Id
     */
    private String guarantorId;
    /**
     * 产品状态
     * 0为未审核，1为审核通过，2为审核失败，3为已被认购，4为过期
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
    private Timestamp createTime;
    /**
     * 修改时间（最后一次状态改变时间）
     */
    private Timestamp updateTime;

    @Override
    public String toString() {
        return "{" +
                "productId=" + productId +
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
