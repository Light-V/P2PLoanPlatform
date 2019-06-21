package com.scut.p2ploanplatform.controller;

import com.scut.p2ploanplatform.entity.CreditInfo;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.form.CreditInfoForm;
import com.scut.p2ploanplatform.service.CreditService;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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
    @GetMapping
    public ResultVo getCreditInfo(HttpSession session) {
        String userId = (String)session.getAttribute("user");
        CreditInfo result = null;
        try {
            result = creditService.getCreditInfo(userId);
        }
        catch (Exception exception){
            //TODO
        }

        ResultVo resultVo = new ResultVo();
        resultVo.setData(result);
        resultVo.setMsg("success");
        return resultVo;
    }

    @RequestMapping("/update_credit_info")
    @PostMapping
    public ResultVo updateCreditInfo(@Valid CreditInfoForm form, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()){
            String error_msg = bindingResult.getFieldError().getDefaultMessage();
            return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID);
        }
        String userId = (String)session.getAttribute("user");

        BigDecimal result = null;
        try {
            if (creditService.updateCreditInfo(userId, form)){
                //用户信息被修改
                BigDecimal rate = creditService.creditReport(userId);
                result = creditService.creditGrant(userId, rate);
            }
            else{
                //未被修改
                result = creditService.getGrantInfo(userId);
            }
        }
        catch (SQLException exception){
        }

        Map<String, Object> map = new HashMap<>();
        map.put("quota", result);
        return ResultVoUtil.success(map);
    }


}
