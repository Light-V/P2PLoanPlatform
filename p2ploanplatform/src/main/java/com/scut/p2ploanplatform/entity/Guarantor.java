package com.scut.p2ploanplatform.entity;

import lombok.Data;

/**
 * @author: Light
 * @date: 2019/6/17 09:59
 * @description:
 */

@Data
public class Guarantor {

    /**
     * 担保人Id
     */
    private String guarantorId;

    /**
     * 担保人密码
     */
    private String password;

    /**
     * 担保人姓名
     */
    private String name;

    /**
     * 担保人第三方担保账户Id
     */
    private String thirdPartyId;

    /**
     * 担保人授权等级，涉及最大担保金额计算
     */
    private Integer authorityId;

}
