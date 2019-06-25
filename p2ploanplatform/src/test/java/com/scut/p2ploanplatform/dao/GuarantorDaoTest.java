package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.Guarantor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @author: zrh
 * @date: 2019/6/25 11:17
 * @description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GuarantorDaoTest {

    @Autowired
    private GuarantorDao guarantorDao;

    private Guarantor guarantor;


    @Before
    public void setUp() throws Exception {
        guarantor = new Guarantor();
        guarantor.setGuarantorId("123");
        guarantor.setThirdPartyId("111");
        guarantor.setPassword("123456");
        guarantor.setAuthorityId(1);
        guarantor.setName("zhou");
    }

    @Test
    @Transactional
    public void insertGuarantor() {
        int result = guarantorDao.insertGuarantor(guarantor);
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void updateGuarantor() {
        guarantorDao.insertGuarantor(guarantor);
        guarantor.setName("zheng");
        guarantor.setAuthorityId(2);
        guarantor.setThirdPartyId("222");
        guarantor.setPassword("654321");
        guarantorDao.updateGuarantor(guarantor);
        Guarantor it = guarantorDao.selectGuarantor(guarantor.getGuarantorId());
        assertEquals(it,guarantor);

    }

    @Test
    @Transactional
    public void selectGuarantor() {
        guarantorDao.insertGuarantor(guarantor);
        Guarantor it = guarantorDao.selectGuarantor("123");
        assertEquals(it,guarantor);
    }

    @Test
    @Transactional
    public void deleteGuarantor() {
        guarantorDao.insertGuarantor(guarantor);
        guarantorDao.deleteGuarantor(guarantor.getGuarantorId());
        Guarantor it = guarantorDao.selectGuarantor("123");
        assertEquals(null,it);
    }
}