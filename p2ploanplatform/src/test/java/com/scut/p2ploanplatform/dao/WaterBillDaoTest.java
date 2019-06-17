package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.WaterBill;
import com.scut.p2ploanplatform.enums.PayModeEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by zonghang
 * Date 2019/6/14 15:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class WaterBillDaoTest {

    @Autowired
    private WaterBillDao waterBillDao;

    @Test
    @Transactional
    public void insertTest() {
        WaterBill waterBill = new WaterBill();
        String uuid = UUID.randomUUID().toString().replace("-", "");
        waterBill.setWaterBillId(uuid);
        waterBill.setPayeeId("123456789012");
        waterBill.setPayerId("098765432109");
        waterBill.setAmount(new BigDecimal(10000));
        waterBill.setMode(0);
        waterBill.setTime(new Date());
        int result = waterBillDao.insert(waterBill);
        assertEquals(1, result);
    }

    @Test
    public void findByPayeeIdTest() {
        List<WaterBill> waterBillList = waterBillDao.findByPayeeId("123456789012");
        for (WaterBill waterBill : waterBillList) {
            assertNotNull(waterBill.getWaterBillId());
            assertNotNull(waterBill.getPayeeId());
            assertNotNull(waterBill.getPayerId());
            assertNotNull(waterBill.getAmount());
            assertNotNull(waterBill.getMode());
            assertNotNull(waterBill.getTime());
        }
    }

    @Test
    public void findByPayerIdTest() {
        List<WaterBill> waterBillList = waterBillDao.findByPayerId("098765432109");
        for (WaterBill waterBill : waterBillList) {
            assertNotNull(waterBill.getWaterBillId());
            assertNotNull(waterBill.getPayeeId());
            assertNotNull(waterBill.getPayerId());
            assertNotNull(waterBill.getAmount());
            assertNotNull(waterBill.getMode());
            assertNotNull(waterBill.getTime());
        }
    }

    @Test
    public void findByModeTest() {
        List<WaterBill> waterBillList = waterBillDao.findByMode(PayModeEnum.LOAN.getCode());
        for (WaterBill waterBill : waterBillList) {
            assertNotNull(waterBill.getWaterBillId());
            assertNotNull(waterBill.getPayeeId());
            assertNotNull(waterBill.getPayerId());
            assertNotNull(waterBill.getAmount());
            assertNotNull(waterBill.getMode());
            assertNotNull(waterBill.getTime());
        }
    }
}