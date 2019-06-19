package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.entity.WaterBill;
import com.scut.p2ploanplatform.enums.PayModeEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zonghang
 * Date 2019/6/18 16:55
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WaterBillServiceImplTest {

    @Autowired
    private WaterBillServiceImpl waterBillService;

    private final String PAYEE_ID = "123456789098";

    private final String PAYER_ID = "098765432123";

    @Test
    @Transactional
    public void addNewWaterBill() {
        int result = waterBillService.addNewWaterBill(PAYEE_ID, PAYER_ID, new BigDecimal(10000), PayModeEnum.LOAN, new Date());
        assertEquals(1, result);
    }

    @Test
    public void findUserPayRecord() {
        PageInfo<WaterBill> waterBillPageInfo = waterBillService.findUserPayRecord(PAYER_ID, 1, 9);
        assertNotNull(waterBillPageInfo.getList());
        assertEquals(9, waterBillPageInfo.getPageSize());
        assertEquals(1, waterBillPageInfo.getPageNum());
    }

    @Test
    public void findUserIncomeRecord() {
        PageInfo<WaterBill> waterBillPageInfo = waterBillService.findUserPayRecord(PAYEE_ID, 1, 9);
        assertNotNull(waterBillPageInfo.getList());
        assertEquals(9, waterBillPageInfo.getPageSize());
        assertEquals(1, waterBillPageInfo.getPageNum());
    }

    @Test
    @Transactional
    public void findWaterBillByModeAndTime() throws ParseException {
        waterBillService.addNewWaterBill(PAYEE_ID, PAYER_ID, new BigDecimal(1000), PayModeEnum.LOAN,
                DateFormat.getDateTimeInstance().parse("2019-6-15 01:20:34"));
        waterBillService.addNewWaterBill(PAYEE_ID, PAYER_ID, new BigDecimal(1000), PayModeEnum.LOAN,
                DateFormat.getDateTimeInstance().parse("2019-6-19 00:00:00"));
        waterBillService.addNewWaterBill(PAYEE_ID, PAYER_ID, new BigDecimal(1000), PayModeEnum.LOAN,
                DateFormat.getDateTimeInstance().parse("2019-6-19 11:21:34"));
        waterBillService.addNewWaterBill(PAYEE_ID, PAYER_ID, new BigDecimal(1000), PayModeEnum.LOAN,
                DateFormat.getDateTimeInstance().parse("2019-6-19 23:59:59"));
        waterBillService.addNewWaterBill(PAYEE_ID, PAYER_ID, new BigDecimal(1000), PayModeEnum.LOAN,
                DateFormat.getDateTimeInstance().parse("2019-6-20 00:00:00"));
        List<WaterBill> waterBillList = waterBillService.findWaterBillByModeAndTime(PayModeEnum.LOAN, "2019-6-19", "2019-6-19");
        assertNotNull(waterBillList);
    }
}