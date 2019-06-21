package com.scut.p2ploanplatform.enums;

import lombok.Getter;

@Getter
public enum AccountTypeEnum {
    NORMAL(0,"普通账户"),
    RISKRESERVE(1,"风险准备金账户")
    ;
    private Integer code;

    private String msg;

    public Integer getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }
    AccountTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
