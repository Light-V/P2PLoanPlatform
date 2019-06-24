package com.scut.p2ploanplatform.service;

import java.sql.SQLException;
import com.scut.p2ploanplatform.entity.User;

/**
 * @author: zrh
 * @date: 2019/6/18
 * @description:处理用户登录注册活动
 */


public interface UserService {
    /**
     * 添加新注册用户
     * @param userId 用户ID
     * @param departmentId 部门ID
     * @param password 用户密码
     * @param phone 用户手机号码
     * @param idCard 身份证号码
     * @param thirdPartyId 第三方账号
     * @param name 用户姓名
     * @param address 用户地址
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 添加成功后返回1,失败返回0
     */
    int insertUser(String userId, int departmentId, String password, String phone, String idCard, String thirdPartyId, String name, String address)
            throws SQLException, IllegalArgumentException;

    /**
     * 根据用户账号查找用户资料
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 返回对应的User
     */
    User findUser(String userId) throws SQLException, IllegalArgumentException;

    /**
     * 用户修改密码
     * @param userId 用户ID
     * @param password 用户密码
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 修改成功后返回1,失败返回0
     */
    int updataPassword(String userId, String password) throws SQLException, IllegalArgumentException;

    /**
     * 修改用户资料，包括手机号和地址
     * @param userId 用户ID
     * @param phone 用户手机号码
     * @param address 用户地址
     * @throws SQLException SQL错误
     * @throws IllegalArgumentException 参数错误
     * @return 修改成功后返回1,失败返回0
     */
    int updataUser(String userId, String phone, String address) throws SQLException, IllegalArgumentException;
}
