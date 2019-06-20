package com.scut.p2ploanplatform.enums;

import lombok.Getter;

/**
 * 借款状态枚举类
 *
 * @author FatCat
 */

@Getter
public enum LoanStatus {
    /**
     * 未审核
     */
    UNREVIEWED(0),
    /**
     * 审核已通过(上架)
     */
    REVIEWED_PASSED(1),
    /**
     * 审核未通过
     */
    REVIEWED_REJECTED(2),
    /**
     * 已认购(合约中）
     */
    SUBSCRIBED(3),
    /**
     * 已过期（未认购)
     */
    EXPIRED(4),
    /**
     * 逾期
     */
    OVERDUE(5),
    /**
     * 合约结束
     */
    FINISHED(6)
    ;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    LoanStatus(Integer status) {
        this.status = status;
    }
}
