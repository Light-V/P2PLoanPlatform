package com.scut.p2ploanplatform.entity;

import lombok.Data;

/**
 * @author: Light
 * @date: 2019/6/17 09:54
 * @description:
 */

@Data
public class User {

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 用户所在部门代码
     */
    private Integer departmentId;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户手机号码
     */
    private String phone;

    /**
     * 用户身份证号码
     */
    private String idCard;

    /**
     * 用户第三方账户Id
     */
    private String thirdPartyId;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户地址
     */
    private String address;

}
