package com.scut.p2ploanplatform.controller;

import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.CustomException;
import com.scut.p2ploanplatform.service.PurchaseService;
import com.scut.p2ploanplatform.service.RepayService;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repay_plan")
public class RepayPlanController {

    private RepayService repayService;
    private PurchaseService purchaseService;

    @Autowired
    public void setRepayService(RepayService repayService, PurchaseService purchaseService) {
        this.repayService = repayService;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/{purchaseId}")
    public ResultVo listRepayPlan(@PathVariable("purchaseId") Integer purchaseId, @SessionAttribute("user") String userId) throws CustomException {
        try {
            Purchase purchase = purchaseService.showPurchaseById(purchaseId);
            if (purchase == null)
                return ResultVoUtil.error(ResultEnum.PURCHASE_NOT_EXITST);
            if (!purchase.getInvestorId().equals(userId))
                return ResultVoUtil.error(ResultEnum.ILLEGAL_OPERATION);
            List<RepayPlan> plans = repayService.findPlanByPurchaseId(purchaseId);
            return ResultVoUtil.success(plans);
        } catch (Exception e) {
            throw new CustomException(ResultEnum.ILLEGAL_OPERATION);
        }
    }
}
