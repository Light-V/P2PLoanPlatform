package com.scut.p2ploanplatform.enums;

import lombok.Getter;

@Getter
public enum P2pAccountStatusEnum {
    FROZEN(0,"冻结"),
    ACTIVE(1,"激活")
    ;
    private Integer code;

    private String msg;

    P2pAccountStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
