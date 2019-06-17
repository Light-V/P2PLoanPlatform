package com.scut.p2ploanplatform.entity;

import lombok.Data;

/**
 * @auther: Light
 * @date: 2019/6/17 09:59
 * @description:
 */

@Data
public class Guarantor {

    String guarantorId;

    String password;

    String name;

    String thirdPartyId;

    Integer authorityId;

}
