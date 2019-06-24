package com.scut.p2ploanplatform.controller;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.form.ApplicationInfoForm;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.utils.ParamModel;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/loan_application")
public class ApplicationController {

    @Autowired
    LoanApplicationService applicationService;

    @RequestMapping("/new")
    @GetMapping
    public ResultVo createApplication(@Valid @ParamModel ApplicationInfoForm form, HttpSession session){

        String userId = (String)session.getAttribute("user");
        LoanApplication application = new LoanApplication();

        try{
            application.setBorrowerId(userId);
            application.setTitle(form.getTitle());
            application.setAmount(form.getAmount());
            application.setInterestRate(form.getInterestRate());
            application.setLoanMonth(form.getLoanMonth());
            application.setPurchaseDeadline(form.getPurchaseDeadline());
            application.setStatus(LoanStatus.UNREVIEWED.getStatus());
            applicationService.addApplication(application);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(),e.getLocalizedMessage());
        }
        return ResultVoUtil.success();
    }
}
