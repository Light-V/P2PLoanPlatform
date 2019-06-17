package com.scut.p2ploanplatform.entity;

import lombok.Data;

/**
 * @auther: Light
 * @date: 2019/6/17 09:54
 * @description:
 */

@Data
public class User {

    String userId;

    Integer departmentId;

    String password;

    String phone;

    String idCard;

    String thirdPartyId;

    String name;

    String address;

}
