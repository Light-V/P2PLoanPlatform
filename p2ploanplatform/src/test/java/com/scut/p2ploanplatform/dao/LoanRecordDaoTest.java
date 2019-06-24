package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.entity.LoanRecord;
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
 * Date 2019/6/20 23:11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoanRecordDaoTest {

    @Autowired
    private LoanRecordDao loanRecordDao;

    private final String BORROWER_ID = "123456789098";
    private final String INVESTOR_ID = "098765432123";

    private LoanRecord createRecord() {
        LoanRecord loanRecord = new LoanRecord();
        loanRecord.setRecordId(UUID.randomUUID().toString().replace("-", ""));
        loanRecord.setPurchaseId(1);
        loanRecord.setBorrowerId(BORROWER_ID);
        loanRecord.setInvestorId(INVESTOR_ID);
        loanRecord.setAmount(new BigDecimal(10000));
        loanRecord.setTime(new Date());
        return loanRecord;
    }

    @Test
    @Transactional
    public void insertTest() {
        LoanRecord loanRecord = createRecord();
        int result = loanRecordDao.insert(loanRecord);
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void findByBorrowerIdTest() {
        for (int i = 0; i < 5; ++i) {
            LoanRecord loanRecord = createRecord();
            loanRecordDao.insert(loanRecord);
        }
        List<LoanRecord> loanRecordList = loanRecordDao.findByBorrowerId(BORROWER_ID);
        for (LoanRecord loanRecord : loanRecordList) {
            assertNotNull(loanRecord.getRecordId());
            assertNotNull(loanRecord.getPurchaseId());
            assertEquals(BORROWER_ID, loanRecord.getBorrowerId());
            assertNotNull(loanRecord.getInvestorId());
            assertNotNull(loanRecord.getTime());
            assertNotNull(loanRecord.getAmount());
        }
    }

    @Test
    @Transactional
    public void findByInvestorIdTest() {
        for (int i = 0; i < 5; ++i) {
            LoanRecord loanRecord = createRecord();
            loanRecordDao.insert(loanRecord);
        }
        List<LoanRecord> loanRecordList = loanRecordDao.findByBorrowerId(INVESTOR_ID);
        for (LoanRecord loanRecord : loanRecordList) {
            assertNotNull(loanRecord.getRecordId());
            assertNotNull(loanRecord.getPurchaseId());
            assertNotNull(loanRecord.getBorrowerId());
            assertEquals(INVESTOR_ID, loanRecord.getInvestorId());
            assertNotNull(loanRecord.getTime());
            assertNotNull(loanRecord.getAmount());
        }
    }
}