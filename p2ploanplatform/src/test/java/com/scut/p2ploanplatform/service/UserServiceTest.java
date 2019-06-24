package com.scut.p2ploanplatform.service;


import com.scut.p2ploanplatform.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * @author: zrh
 * @date: 2019/6/19
 * @description:测试UserService
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    private User sampleUser;

    @Before
    public void setUp() throws Exception {
        sampleUser = new User();
        sampleUser.setUserId("2");
        sampleUser.setDepartmentId(2);
        sampleUser.setPassword("123456");
        sampleUser.setPhone("15511223344");
        sampleUser.setThirdPartyId("123");
        sampleUser.setIdCard("445281199709195332");
        sampleUser.setAddress("双鸭山大学");
        sampleUser.setName("zhou");
    }

    @Test
    @Transactional
    public void insertUserTest() throws SQLException, IllegalArgumentException{
        int result = userService.insertUser(sampleUser.getUserId(),sampleUser.getDepartmentId(),sampleUser.getPassword(), sampleUser.getPhone(),sampleUser.getIdCard(),sampleUser.getThirdPartyId(),sampleUser.getName(),sampleUser.getAddress());
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void findUserTest() throws SQLException, IllegalArgumentException{
        int result = userService.insertUser(sampleUser.getUserId(),sampleUser.getDepartmentId(),sampleUser.getPassword(), sampleUser.getPhone(),sampleUser.getIdCard(),sampleUser.getThirdPartyId(),sampleUser.getName(),sampleUser.getAddress());
        assertEquals(sampleUser,userService.findUser(sampleUser.getUserId()));
    }

    @Test
    @Transactional
    public void updataPasswordTest() throws SQLException, IllegalArgumentException{
        userService.insertUser(sampleUser.getUserId(),sampleUser.getDepartmentId(),sampleUser.getPassword(), sampleUser.getPhone(),sampleUser.getIdCard(),sampleUser.getThirdPartyId(),sampleUser.getName(),sampleUser.getAddress());
        sampleUser.setPassword("147");
        userService.updataPassword(sampleUser.getUserId(),sampleUser.getPassword());
        assertEquals(sampleUser,userService.findUser(sampleUser.getUserId()));
    }

    @Test
    @Transactional
    public void updataUserTest() throws SQLException, IllegalArgumentException{
        userService.insertUser(sampleUser.getUserId(),sampleUser.getDepartmentId(),sampleUser.getPassword(), sampleUser.getPhone(),sampleUser.getIdCard(),sampleUser.getThirdPartyId(),sampleUser.getName(),sampleUser.getAddress());
        sampleUser.setPhone("15544332211");
        sampleUser.setAddress("华南理工幼儿园附属大学");
        userService.updataUser(sampleUser.getUserId(),sampleUser.getPhone(),sampleUser.getAddress());
        assertEquals(sampleUser,userService.findUser(sampleUser.getUserId()));
    }
}