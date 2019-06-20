package com.scut.p2ploanplatform.controller;

import com.scut.p2ploanplatform.service.CreditService;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Light
 * @date: 2019/6/20 09:09
 * @description:
 */

@RestController
@RequestMapping("/credit")
public class CreditController {

    @Autowired
    CreditService creditService;

    @RequestMapping("/get_credit_info")
    public ResultVo getCreditInfo() {
        ResultVo vo = new ResultVo();
        return vo;
    }
}
