package com.scut.p2ploanplatform.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.service.GuarantorService;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.service.UserService;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.PageVo;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.scut.p2ploanplatform.utils.ResultVoUtil.error;

/**
 * @author: Light
 * @date: 2019/6/26 10:23
 * @description:
 */

@RestController
@RequestMapping("/guarantor")
public class GuarantorController {
    @Autowired
    LoanApplicationService loanApplicationService;
    @Autowired
    UserService userService;
    @Autowired
    GuarantorService guarantorService;
    @Autowired
    LoanApplicationService applicationService;

    @RequestMapping("/guarantee/detail/{applicationId}")
    @GetMapping
    public ResultVo getDetail(@PathVariable Integer applicationId,
                              @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }

        LoanApplication application;
        try{
            application = applicationService.getApplicationById(applicationId);
        }catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
        }
        if(application == null){
            return error(ResultEnum.APPLICATION_NOT_EXIST);
        }
        return ResultVoUtil.success(application);
    }


    @RequestMapping("/guarantee/unreviewed")
    @GetMapping
    public ResultVo getUnreviewed(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
            @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }

        PageInfo applicationPageInfo;
        try{
            applicationPageInfo = applicationService.getApplicationUnReviewed(pageNum, pageSize);
        }catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
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

    @RequestMapping("/guarantee/review_passed")
    @GetMapping
    public ResultVo getReviewPassed(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                  @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }

        PageInfo applicationPageInfo;
        try{
            applicationPageInfo = applicationService.getApplicationReviewedPassed(pageNum, pageSize);
        }catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
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

    @RequestMapping("/guarantee/review_rejected")
    @GetMapping
    public ResultVo getReviewRejected(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                    @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                    @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }

        PageInfo applicationPageInfo;
        try{
            applicationPageInfo = applicationService.getApplicationReviewedRejected(pageNum, pageSize);
        }catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
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

    @RequestMapping("/guarantee/review_expired")
    @GetMapping
    public ResultVo getReviewExpired(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                      @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }

        PageInfo applicationPageInfo;
        try{
            applicationPageInfo = applicationService.getApplicationReviewExpired(pageNum, pageSize);
        }catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
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

    @RequestMapping("/guarantee/all")
    @GetMapping
    public ResultVo getAllApplication(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                      @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                      @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }

        PageInfo applicationPageInfo;
        try{
            applicationPageInfo = applicationService.getAllApplication(pageNum, pageSize);
        }catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
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

    @RequestMapping("/guarantee/pass")
    @GetMapping
    public ResultVo passApllication(@RequestParam("application_id") Integer applicationId,
                                      @SessionAttribute("user") String userId,
                                      @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }
        try {
            if(applicationService.reviewPass(applicationId, userId)){
                return ResultVoUtil.success();
            }
            else return ResultVoUtil.error(ResultEnum.REVIEW_PRIVILEGE_DENY);
        }
        catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
        }
    }

    @RequestMapping("/guarantee/reject")
    @GetMapping
    public ResultVo rejectApllication(@RequestParam("application_id") Integer applicationId,
                                    @SessionAttribute("user") String userId,
                                    @SessionAttribute(value = "user_type") int userType){
        if (userType != 2) {
            return error(ResultEnum.USER_AUTHORITY_DENY);
        }
        try {
            if (applicationService.reviewReject(applicationId, userId)){
                return ResultVoUtil.success();
            }
            else return ResultVoUtil.error(ResultEnum.REVIEW_PRIVILEGE_DENY);
        }
        catch (Exception e){
            return error(ResultEnum.PARAM_IS_INVALID);
        }
    }

}
