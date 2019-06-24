package com.scut.p2ploanplatform.exception;

import com.scut.p2ploanplatform.enums.ResultEnum;
import lombok.Getter;

/**
 * Created by FatCat
 */
@Getter
public class LoanStatusException extends RuntimeException {

    private Integer code;

    public LoanStatusException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public LoanStatusException(String msg, Integer code) {
        super(msg);
        this.code = code;
    }

}
