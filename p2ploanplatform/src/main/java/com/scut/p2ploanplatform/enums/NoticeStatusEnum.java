package com.scut.p2ploanplatform.enums;

import lombok.Getter;

/**
 * Created by zonghang
 * Date 2019/6/16 10:42
 */
@Getter
public enum NoticeStatusEnum {
    UNREAD(0, "未读"),
    READ(1, "已读"),
    ;
    private Integer code;

    private String msg;

    NoticeStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }}
