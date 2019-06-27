package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @auther: zrh
 * @date: 2019/6/17 16:10
 * @description:
 */


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    private User sampleUser;

    @Before
    public void setUp() throws Exception {
        sampleUser = new User();
        sampleUser.setUserId("2");
        sampleUser.setDepartmentId(2);
        sampleUser.setPassword("123456");
        sampleUser.setPhone("15511223344");
        sampleUser.setThirdPartyId("123");
        sampleUser.setIdCard("456");
        sampleUser.setAddress("双鸭山大学");
        sampleUser.setName("zhou");
    }

    @Test
    @Transactional
    public void insertUserTest() {
        int result = insertUser(sampleUser);
        assertEquals(1, result);
    }

    private int insertUser(User newUser) {
        return userDao.insertUser(newUser);
    }

    @Test
    @Transactional
    public void findUserTest() {

        // check: non-empty result
        insertUser(sampleUser);
        User it = userDao.findUser(sampleUser.getUserId());
        assertEquals(sampleUser, it);
    }

    @Test
    @Transactional
    public void updatePasswordTest() {
        insertUser(sampleUser);
        sampleUser.setPassword("123");
        int ret = userDao.updatePassword(sampleUser);
        assertEquals(1, ret);
        User it = userDao.findUser(sampleUser.getUserId());
        assertEquals("123", it.getPassword());
    }

    @Test
    @Transactional
    public void updateUserTest() {
        insertUser(sampleUser);
        sampleUser.setPhone("15544332211");
        sampleUser.setAddress("华南理工幼儿园附属大学");
        int ret = userDao.updateUser(sampleUser);
        assertEquals(1, ret);
        User it = userDao.findUser(sampleUser.getUserId());
        assertEquals("15544332211", it.getPhone());
        assertEquals("华南理工幼儿园附属大学", it.getAddress());
    }

    @Test
    @Transactional
    public void deleteUserTest() {
        insertUser(sampleUser);
        int result = userDao.deleteUser(sampleUser.getUserId());
        assertEquals(1, result);
    }
}