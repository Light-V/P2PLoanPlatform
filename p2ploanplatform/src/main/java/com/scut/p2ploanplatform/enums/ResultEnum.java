package com.scut.p2ploanplatform.enums;

import lombok.Getter;

/**
 * Created by zonghang
 * Date 2019/6/19 10:23
 */
@Getter
public enum  ResultEnum {
    DATE_ERROR(101, "日期格式错误"),
    ;
    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
