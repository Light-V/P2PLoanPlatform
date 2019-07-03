package com.scut.p2ploanplatform.vo;

import com.scut.p2ploanplatform.entity.Notice;
import com.scut.p2ploanplatform.entity.RepayPlan;
import lombok.Data;

@Data
public class RepayExecutionResultVo {
    RepayPlan repayPlanOld;
    RepayPlan repayPlanNew;
    Notice borrowerNotice;
    Notice guarantorNotice;
    Notice investorNotice;
    ResultVo borrowerTransferResult;
    ResultVo guarantorTransferResult;

}
