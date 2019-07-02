package com.scut.p2ploanplatform.controller;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.LoanStatusException;
import com.scut.p2ploanplatform.service.PurchaseService;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.PageVo;
import com.scut.p2ploanplatform.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@Slf4j//todo:log
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    PurchaseService purchaseService;

    @RequestMapping("/subscribe")
    @PostMapping
    public ResultVo subscribe(@RequestParam(value = "application_id") Integer applicationId,
                              @RequestParam(value = "payment_password") String password,
                              @SessionAttribute(value = "user") String userId){
        try{
            purchaseService.subscribed(userId, applicationId,password);
        }catch (IllegalArgumentException e){
            return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(), e.getMessage());
        }catch (LoanStatusException e){
            return ResultVoUtil.error(ResultEnum.APPLICATION_NOT_PASS_REVIEWED);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.ILLEGAL_OPERATION.getCode(),e.getMessage());
        }
        return ResultVoUtil.success();
    }

    @RequestMapping("/detail/{purchaseId}")
    @GetMapping
    public ResultVo purchaseDetail(@PathVariable Integer purchaseId,
                                   @SessionAttribute(value = "user") String userId){
        Purchase purchase;
        try{
            purchase = purchaseService.showPurchaseById(purchaseId);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(),e.getMessage());
        }
        if(purchase == null){
            return ResultVoUtil.error(ResultEnum.PURCHASE_NOT_EXITST);
        }
        if (!purchase.getBorrowerId().equals(userId)||!purchase.getInvestorId().equals(userId)){
            return ResultVoUtil.error(ResultEnum.ILLEGAL_OPERATION);
        }
        return  ResultVoUtil.success(purchase);
    }

    @RequestMapping("/detail")
    @GetMapping
    public ResultVo purchaseDetailByApplicationId(@RequestParam(value = "application_id") Integer applicationId,
                                   @SessionAttribute(value = "user") String userId){
        Purchase purchase;
        try{
            purchase = purchaseService.showPurchaseByApplicationId(applicationId);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(),e.getMessage());
        }
        if(purchase == null){
            return ResultVoUtil.error(ResultEnum.PURCHASE_NOT_EXITST);
        }
        if (!purchase.getBorrowerId().equals(userId)||!purchase.getInvestorId().equals(userId)){
            return ResultVoUtil.error(ResultEnum.ILLEGAL_OPERATION);
        }
        return  ResultVoUtil.success(purchase);
    }

    @RequestMapping("/all")
    @GetMapping
    public  ResultVo showAllPurchase(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                     @SessionAttribute(value = "user") String userId){
        PageInfo<Purchase> purchasePageInfo;
        try{
            purchasePageInfo = purchaseService.showAllPurchase(pageNum, pageSize);
        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.PURCHASE_NOT_EXITST);
        }
        if(purchasePageInfo == null||purchasePageInfo.getTotal()==0){
            return ResultVoUtil.error(ResultEnum.PURCHASE_NOT_EXITST);
        }

        return ResultVoUtil.success(new PageVo(
                purchasePageInfo.getPages(),
                purchasePageInfo.getTotal(),
                purchasePageInfo.getPageSize(),
                purchasePageInfo.getPageNum(),
                purchasePageInfo.getList()
        ));
    }

    @RequestMapping("user_purchases/{type}/{status}")
    @GetMapping
    public ResultVo showBorrowerApplications(@RequestParam(value = "page_num", defaultValue = "1") Integer pageNum,
                                             @RequestParam(value = "page_size", required = false, defaultValue = "10") Integer pageSize,
                                             @SessionAttribute(value = "user") String userId,
                                             @PathVariable String type,
                                             @PathVariable String status){
        PageInfo<Purchase> purchasePageInfo = null;
        try{
            switch (type){
                case "loan":
                    switch (status){
                        case "all":
                            purchasePageInfo = purchaseService.showPurchaseByBorrowerId(userId, pageNum, pageSize);
                            break;
                        case "subscribed":
                            purchasePageInfo = purchaseService.showPurchaseByBorrowerId(userId,
                                    LoanStatus.SUBSCRIBED, pageNum, pageSize);
                            break;
                        case "finished":
                            purchasePageInfo = purchaseService.showPurchaseByBorrowerId(userId,
                                    LoanStatus.FINISHED, pageNum, pageSize);
                            break;
                    }
                    break;
                case "invest":
                    switch (status){
                        case "all":
                            purchasePageInfo = purchaseService.showPurchaseByInvestorId(userId, pageNum, pageSize);
                            break;
                        case "subscribed":
                            purchasePageInfo = purchaseService.showPurchaseByInvestorId(userId,
                                    LoanStatus.SUBSCRIBED, pageNum, pageSize);
                            break;
                        case "finished":
                            purchasePageInfo = purchaseService.showPurchaseByInvestorId(userId,
                                    LoanStatus.FINISHED, pageNum, pageSize);
                            break;
                    }
                    break;
            }

        }catch (Exception e){
            return ResultVoUtil.error(ResultEnum.PURCHASE_NOT_EXITST);
        }
        if(purchasePageInfo == null||purchasePageInfo.getTotal()==0){
            return ResultVoUtil.error(ResultEnum.PURCHASE_NOT_EXITST);
        }

        return ResultVoUtil.success(new PageVo(
                purchasePageInfo.getPages(),
                purchasePageInfo.getTotal(),
                purchasePageInfo.getPageSize(),
                purchasePageInfo.getPageNum(),
                purchasePageInfo.getList()
        ));
    }
}
