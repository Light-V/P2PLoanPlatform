package com.scut.p2ploanplatform.enums;

import lombok.Getter;

/**
 * Created by zonghang
 * Date 2019/6/14 16:00
 */
@Getter
public enum PayModeEnum {
    LOAN(0, "贷款"),
    REPAY(1, "还款")
    ;
    private Integer code;

    private String msg;

    PayModeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }}
