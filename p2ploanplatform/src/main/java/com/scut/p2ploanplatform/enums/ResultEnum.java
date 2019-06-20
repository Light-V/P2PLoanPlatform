package com.scut.p2ploanplatform.enums;

import lombok.Getter;

/**
 * Created by zonghang
 * Date 2019/6/19 10:23
 */
@Getter
public enum  ResultEnum {
    SUCCESS(0, "成功"),

    /* 参数错误：10001-19999 */
    PARAM_IS_INVALID(10001, "参数格式不正确"),
    PARAM_TYPE_BIND_ERROR(10002, "参数类型转换错误"),

    /* 用户错误：20001-29999*/
    USER_NOT_LOGIN(20001, "用户未登录"),

    /* 资源不存在：30001-39999*/
    NOTICE_NOT_EXIST(30001, "通知不存在"),
    ;
    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}