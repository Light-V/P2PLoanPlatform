package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.service.WaterBillService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zonghang
 * Date 2019/6/20 15:27
 */
@Service
public class WaterBillServiceImpl implements WaterBillService {
    @Override
    public void addLoanRecord(Integer purchaseId, String borrowerId, String investorId, Date time, BigDecimal amount) {

    }

    @Override
    public void addRepayRecord(String planId, Integer purchaseId, String payerId, String payeeId, Date time, BigDecimal amount) {

    }
}
