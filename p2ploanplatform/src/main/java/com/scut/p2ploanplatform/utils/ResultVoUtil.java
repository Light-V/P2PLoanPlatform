package com.scut.p2ploanplatform.utils;

import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.vo.ResultVo;

/**
 * Created by zonghang
 * Date 2019/6/20 15:45
 */
public class ResultVoUtil {

    public static ResultVo success(Object o) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(ResultEnum.SUCCESS.getCode());
        resultVo.setMsg(ResultEnum.SUCCESS.getMsg());
        resultVo.setData(o);
        return resultVo;
    }

    public static ResultVo success() {
        return success(null);
    }

    public static ResultVo error(Integer code, String msg) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(code);
        resultVo.setMsg(msg);
        return resultVo;
    }

    public static ResultVo error(ResultEnum resultEnum) {
        return error(resultEnum.getCode(), resultEnum.getMsg());
    }
}
