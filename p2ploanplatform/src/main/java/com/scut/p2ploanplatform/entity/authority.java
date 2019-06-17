package com.scut.p2ploanplatform.entity;

import lombok.Data;

/**
 * @auther: Light
 * @date: 2019/6/17 15:30
 * @description:
 */

@Data
public class authority {

    /**
     * 权限Id
     */
    private Integer authorityId;

    /**
     * 授权最大金额
     */
    private Double authorityAmount;
    
}
