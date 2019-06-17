package com.scut.p2ploanplatform.enums;

import lombok.Getter;

/**
 * 借款申请状态枚举类
 *
 * @author FatCat
 */

@Getter
public enum LoanApplicationStatus {
    /**
     * 未审核
     */
    UNREVIEWED(0),
    /**
     * 审核已通过
     */
    REVIEWED_PASSED(1),
    /**
     * 审核未通过
     */
    REVIEDED_REJECTED(2),
    /**
     * 已认购
     */
    SUBSCRIBED(3),
    /**
     * 已过期
     */
    EXPRIED(4)
    ;

    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    LoanApplicationStatus(Integer status) {
        this.status = status;
    }
}
