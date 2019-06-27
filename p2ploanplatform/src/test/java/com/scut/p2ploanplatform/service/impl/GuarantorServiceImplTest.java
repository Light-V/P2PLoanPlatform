package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.Guarantor;
import com.scut.p2ploanplatform.service.GuarantorService;
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
 * @date: 2019/6/27
 * @description:测试GuarantorService
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GuarantorServiceImplTest {

    @Autowired
    private GuarantorService guarantorService;

    Guarantor guarantor;

    @Before
    public void setUp() throws Exception {
        guarantor = new Guarantor();
        guarantor.setName("zhou");
        guarantor.setAuthorityId(1);
        guarantor.setPassword("123");
        guarantor.setThirdPartyId("123456123456");
        guarantor.setGuarantorId("111");
    }

    @Test
    @Transactional
    public void insertGuarantorTest() throws SQLException, IllegalArgumentException{
        int result = guarantorService.insertGuarantor(guarantor.getGuarantorId(),guarantor.getPassword(),guarantor.getName(),guarantor.getThirdPartyId(),guarantor.getAuthorityId());
        //result = guarantorService.insertGuarantor(guarantor.getGuarantorId(),guarantor.getPassword(),guarantor.getName(),guarantor.getThirdPartyId(),guarantor.getAuthorityId());
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void findGuarantorTest() throws SQLException, IllegalArgumentException{
//        guarantorService.insertGuarantor(guarantor.getGuarantorId(),guarantor.getPassword(),guarantor.getName(),guarantor.getThirdPartyId(),guarantor.getAuthorityId());
//        Guarantor it =guarantorService.findGuarantor(guarantor.getGuarantorId());
//        assertEquals(guarantor, it);
        Guarantor it =guarantorService.findGuarantor(guarantor.getGuarantorId());
        assertEquals(null, it);
    }
}