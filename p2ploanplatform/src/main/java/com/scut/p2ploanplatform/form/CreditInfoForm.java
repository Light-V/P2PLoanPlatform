package com.scut.p2ploanplatform.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * @author: Light
 * @date: 2019/6/20 16:33
 * @description:
 */

@Data
public class CreditInfoForm {
    /**
     * 收入（单位:人民币）
     */
    @NotNull(message = "收入不能为空")
    @DecimalMin("0")
    private BigDecimal income;


    /**
     * 家庭收入 （单位:人民币）
     */
    @NotNull(message = "家庭收入不能为空")
    @DecimalMin("0")
    private BigDecimal familyIncome;

    /**
     *资产 （单位:人民币）
     */
    @NotNull(message = "资产不能为空")
    @DecimalMin("0")
    private BigDecimal assets;

    /**
     * 家庭成员人数
     */
    @NotNull(message = "家庭人数不能为空")
    @DecimalMin("0")
    private Integer familyNumber;

    /**
     * 负载 （单位:人民币）
     */
    @NotNull(message = "负债不能为空")
    @DecimalMin("0")
    private BigDecimal debt;

}
