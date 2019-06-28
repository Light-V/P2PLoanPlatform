package com.scut.p2ploanplatform.form;

import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
@Data
public class ApplicationInfoForm {
    /**
     * 借款标题
     */
    @NotEmpty(message = "借款标题不能为空")
    private String title;
    /**
     * 借款金额
     */
    @NotNull(message = "借款金额不能为空")
    @DecimalMin("0")
    private BigDecimal amount;

    /**
     * 利率
     */
    @NotNull(message = "利率不能为空")
    @DecimalMin("0")
    private BigDecimal interestRate;

    /**
     * 借款月数
     */
    @NotNull(message = "借款时长不能为空")
    @Min(0)
    private Integer loanMonth;

    /**
     * 认购期限
     */
    @NotNull(message = "认购期限不能为空")
    @Future
    private Date purchaseDeadline;
}
