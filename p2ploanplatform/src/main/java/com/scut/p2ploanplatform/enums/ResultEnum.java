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
    APPLICATION_NOT_EXIST(30002,"借款申请不存在"),
    PURCHASE_NOT_EXITST(30003,"订单不存在"),

    /*借款申请、订单错误：40001-49999*/
    APPLICATION_NOT_REVIEWED(40001,"借款申请未审核"),
    APPLICATION_NOT_PASS_REVIEWED(40002,"借款申请未发布"),
    APPLICATION_REJECTED(40003,"借款申请已被拒绝"),
    APPLICATION_EXPIRED(40004,"借款申请已过期"),
    PURCHASE_NOT_SUBSCRIBED(40010,"订单状态异常"),
    PURCHASE_ACCOMPLISHED(40011,"订单已终止"),
    ILLEGAL_OPERATION(40012,"非法操作"),


    UNHANDLED_EXCEPTION(50000,"未知错误")
    ;
    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
