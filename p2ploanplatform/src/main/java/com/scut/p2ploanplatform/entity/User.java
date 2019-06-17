package com.scut.p2ploanplatform.entity;

import lombok.Data;

/**
 * @auther: Light
 * @date: 2019/6/17 09:54
 * @description:
 */

@Data
public class User {

    /**
     * 用户Id
     */
    String userId;

    /**
     * 用户所在部门代码
     */
    Integer departmentId;

    /**
     * 用户密码
     */
    String password;

    /**
     * 用户手机号码
     */
    String phone;

    /**
     * 用户身份证号码
     */
    String idCard;

    /**
     * 用户第三方账户Id
     */
    String thirdPartyId;

    /**
     * 用户姓名
     */
    String name;

    /**
     * 用户地址
     */
    String address;

}
