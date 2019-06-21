package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.RepayRecord;
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
 * Date 2019/6/20 23:51
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RepayRecordDaoTest {

    @Autowired
    private RepayRecordDao repayRecordDao;

    private final String PAYER_ID = "123456789098";
    private final String PAYEE_ID = "098765432123";

    private RepayRecord createRecord() {
        RepayRecord repayRecord = new RepayRecord();
        repayRecord.setRecordId(UUID.randomUUID().toString().replace("-", ""));
        repayRecord.setPurchaseId(1);
        repayRecord.setPlanId("uuid");
        repayRecord.setPayeeId(PAYEE_ID);
        repayRecord.setPayerId(PAYER_ID);
        repayRecord.setAmount(new BigDecimal(10000));
        repayRecord.setTime(new Date());
        return repayRecord;
    }

    @Test
    @Transactional
    public void insertTest() {
        RepayRecord record = createRecord();
        int result = repayRecordDao.insert(record);
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void findByPayeeIdTest() {
        for (int i = 0; i < 5; ++i) {
            repayRecordDao.insert(createRecord());
        }
        List<RepayRecord> repayRecordList = repayRecordDao.findByPayeeId(PAYEE_ID);
        for (RepayRecord repayRecord : repayRecordList) {
            assertNotNull(repayRecord.getRecordId());
            assertNotNull(repayRecord.getPurchaseId());
            assertNotNull(repayRecord.getPlanId());
            assertEquals(PAYEE_ID, repayRecord.getPayeeId());
            assertNotNull(repayRecord.getPayerId());
            assertNotNull(repayRecord.getTime());
            assertNotNull(repayRecord.getAmount());
        }
    }

    @Test
    @Transactional
    public void findByPayerIdTest() {
        for (int i = 0; i < 5; ++i) {
            repayRecordDao.insert(createRecord());
        }
        List<RepayRecord> repayRecordList = repayRecordDao.findByPayerId(PAYER_ID);
        for (RepayRecord repayRecord : repayRecordList) {
            assertNotNull(repayRecord.getRecordId());
            assertNotNull(repayRecord.getPurchaseId());
            assertNotNull(repayRecord.getPlanId());
            assertNotNull(repayRecord.getPayeeId());
            assertEquals(PAYER_ID, repayRecord.getPayerId());
            assertNotNull(repayRecord.getTime());
            assertNotNull(repayRecord.getAmount());
        }
    }

}