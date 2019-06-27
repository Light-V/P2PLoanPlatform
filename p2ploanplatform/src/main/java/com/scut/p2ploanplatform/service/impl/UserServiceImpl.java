package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.UserDao;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;

/**
 * @author: zrh
 * @date: 2019/6/18
 * @description:处理用户登录注册活动
 */

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public int insertUser(String userId, int departmentId, String password, String phone, String idCard, String thirdPartyId, String name, String address)
            throws SQLException, IllegalArgumentException {
        User user = userDao.findUser(userId);
        if (user == null) {
            user = new User();
            user.setUserId(userId);
            user.setDepartmentId(departmentId);
            user.setPassword(password);
            user.setPhone(phone);
            user.setIdCard(idCard);
            user.setThirdPartyId(thirdPartyId);
            user.setName(name);
            user.setAddress(address);
            return userDao.insertUser(user);
        } else {
            return 0;
        }
    }

    @Override
    public User findUser(String userId) throws SQLException, IllegalArgumentException {
        User user = userDao.findUser(userId);
        return user;
    }

    @Override
    public int updataPassword(String userId, String password) throws SQLException, IllegalArgumentException {
        User user = userDao.findUser(userId);
        if(user != null){
            user.setPassword(password);
            return userDao.updatePassword(user);
        } else {
            return 0;
        }
    }

    @Override
    public int updataUser(String userId, String phone, String address) throws SQLException, IllegalArgumentException {
        User user = userDao.findUser(userId);
        if(user != null){
            user.setPhone(phone);
            user.setAddress(address);
            return userDao.updateUser(user);
        } else {
            return 0;
        }
    }

    @Override
    public int deleteUser(String userId) throws SQLException, IllegalArgumentException {
        int result = userDao.deleteUser(userId);
        return result;
    }
}
