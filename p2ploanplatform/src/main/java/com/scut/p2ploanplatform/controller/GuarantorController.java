package com.scut.p2ploanplatform.controller;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.service.UserService;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.PageVo;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping("/requests")
    @GetMapping
    public ResultVo requests(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                             @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                             @SessionAttribute(value = "user") String userId) {
        //验证是否为担保人用户
        //TODO

        try {
            PageInfo applicationPageInfo = loanApplicationService.getApplicationUnReviewed(pageNum, pageSize);
            return ResultVoUtil.success(new PageVo(
                    applicationPageInfo.getPages(),
                    applicationPageInfo.getTotal(),
                    applicationPageInfo.getPageSize(),
                    applicationPageInfo.getPageNum(),
                    applicationPageInfo.getList()
            ));
        }
        catch (Exception e){

        }
        return null;
    }

    @RequestMapping("/pass")
    @GetMapping
    public ResultVo pass(@RequestParam Integer productId,
                         @SessionAttribute(value = "user") String userId) {
        try {
            if (loanApplicationService.reviewPass(productId, userId)){
                return ResultVoUtil.success();
            }
            else{
                return ResultVoUtil.error(ResultEnum.UNHANDLED_EXCEPTION);
            }
        }
        catch (Exception e) {
            return null;
        }
    }

    @RequestMapping("/reject")
    @GetMapping
    public ResultVo reject(@RequestParam Integer productId,
                         @SessionAttribute(value = "user") String userId) {
        try {
            if (loanApplicationService.reviewReject(productId, userId)){
                return ResultVoUtil.success();
            }
            else{
                return ResultVoUtil.error(ResultEnum.UNHANDLED_EXCEPTION);
            }
        }
        catch (Exception e) {
            return null;
        }
    }
}
