package com.scut.p2ploanplatform.vo;

import lombok.Data;

@Data
public class ResultVo {
    //状态码
    private int code;
    //返回消息
    private String msg;
    //返回数据
    private Object data;
}
