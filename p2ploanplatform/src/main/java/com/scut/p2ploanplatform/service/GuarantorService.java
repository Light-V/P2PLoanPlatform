package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.entity.Guarantor;

import java.sql.SQLException;

public interface GuarantorService {
    /**
     * 添加新注册用户
     * @param guarantorId 用户ID
     * @param password 用户密码
     * @param thirdPartyId 第三方账号
     * @param name 用户姓名
     * @param authorityId 权限等级
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 添加成功后返回1,失败返回0
     */
    int insertGuarantor(String guarantorId, String password, String name, String thirdPartyId, int authorityId)
            throws SQLException, IllegalArgumentException;

    /**
     * 根据用户账号查找用户资料
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 返回对应的User
     */
    Guarantor findGuarantor(String guarantorId) throws SQLException, IllegalArgumentException;

    /**
     * 修改用户资料，包括手机号和地址
     * @param guarantorId 用户ID
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 修改成功后返回1,失败返回0
     */
    int updataGuarantor(String guarantorId) throws SQLException, IllegalArgumentException;

    /**
     * 删除担保人
     * @param guarantorId 用户ID
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 添加成功后返回1,失败返回0
     */
    int deleteGuarantor ( String guarantorId ) throws SQLException, IllegalArgumentException;

}
