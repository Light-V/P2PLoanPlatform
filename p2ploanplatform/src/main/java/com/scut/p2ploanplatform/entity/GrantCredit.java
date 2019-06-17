package com.scut.p2ploanplatform.entity;

import lombok.Data;

import java.util.Date;

/**
 * @auther: Light
 * @date: 2019/6/17 11:04
 * @description:
 */

@Data
public class GrantCredit {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 个人收入 （单位:人民币）
     */
    private Double income;

    /**
     * 授信额度 （单位:人民币）
     */
    private Double quota;

    /**
     *最高利率
     */
    private Double rate;

    /**
     * 授信有效期限
     */
    private Date expire;
}
