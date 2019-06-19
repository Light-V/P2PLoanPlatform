package com.scut.p2ploanplatform.exception;

import com.scut.p2ploanplatform.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by zonghang
 * Date 2019/6/19 10:16
 */
@Getter
public class CustomException extends RuntimeException {

    private Integer code;

    public CustomException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public CustomException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

}
