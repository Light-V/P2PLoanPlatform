package com.scut.p2ploanplatform.enums;

/**
 * 还款状态的枚举类
 */
public enum RepayPlanStatus {
    /**
     * 已计划，未到还款时间
     */
    SCHEDULED(0),
    /**
     * 还款成功
     */
    SUCCEEDED(1),
    /**
     * 已逾期
     */
    OVERDUE(2);


    private Integer status;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    RepayPlanStatus(Integer status) {
        this.status = status;
    }
}
