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


@SpringBootTest
@RunWith(SpringRunner.class)
public class SignUpDaoTest {

    @Autowired
    private SignUpDao signUpDao;

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
        return signUpDao.insertUser(newUser);
    }

    @Test
    @Transactional
    public void findUserIdTest() {

        // check: non-empty result
        insertUser(sampleUser);
        User it = signUpDao.findUser(sampleUser.getUserId());
        assertEquals(sampleUser, it);
    }
}