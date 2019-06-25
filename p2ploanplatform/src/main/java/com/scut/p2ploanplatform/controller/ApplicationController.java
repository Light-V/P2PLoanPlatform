package com.scut.p2ploanplatform.controller;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.form.ApplicationInfoForm;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.utils.ParamModel;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.PageVo;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/loan_application")
public class ApplicationController {

    @Autowired
    LoanApplicationService applicationService;

    @RequestMapping("/new")
    @PostMapping
    public ResultVo createApplication(@Valid @ParamModel ApplicationInfoForm form, HttpSession session){

        String userId;
        try{
            userId = (String)session.getAttribute("user");
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.USER_NOT_LOGIN);
        }

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

    @RequestMapping("/cancel")
    @GetMapping
    public ResultVo cancelApplication(HttpServletRequest request, HttpSession session){

        String userId;
        Integer applicationId = Integer.valueOf(request.getParameter("application_id"));
        LoanApplication application;

        try{
            userId = (String)session.getAttribute("user");
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.USER_NOT_LOGIN);
        }

        try{
            application = applicationService.getApplicationById(applicationId);
            if(userId.equals(application.getBorrowerId())){
                return ResultVoUtil.error(ResultEnum.ILLEGAL_OPERATION.getCode(), "你不是该订单的发起人，不能对该订单进行操作");
            }
        }catch (SQLException e){
            return  ResultVoUtil.error(ResultEnum.APPLICATION_NOT_EXIST);
        }

        try{
            applicationService.expire(applicationId);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(),e.getLocalizedMessage());
        }

        return ResultVoUtil.success();
    }

    @RequestMapping(value = "/detail/{applicationId}")
    @GetMapping
    public ResultVo applicationDetail(@PathVariable Integer applicationId){
        LoanApplication application;
        try{
            application = applicationService.getApplicationById(applicationId);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(),e.getLocalizedMessage());
        }
        if(application == null){
            return ResultVoUtil.error(ResultEnum.APPLICATION_NOT_EXIST);
        }
        return  ResultVoUtil.success(application);
    }

    @RequestMapping("/user_applications/all")
    @GetMapping
    public ResultVo showAllApplications(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                        @RequestParam(value = "page_size", defaultValue = "30") Integer pageSize){
        PageInfo<LoanApplication> applicationPageInfo = null;
        try{
            applicationPageInfo = applicationService.getAllApplication(pageNum, pageSize);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.APPLICATION_NOT_EXIST);
        }
        if(applicationPageInfo == null||applicationPageInfo.getTotal()==0){
            return ResultVoUtil.error(ResultEnum.APPLICATION_NOT_EXIST);
        }

        return ResultVoUtil.success(new PageVo(
                applicationPageInfo.getPages(),
                applicationPageInfo.getTotal(),
                applicationPageInfo.getPageSize(),
                applicationPageInfo.getPageNum(),
                applicationPageInfo.getList()
        ));
    }

}
