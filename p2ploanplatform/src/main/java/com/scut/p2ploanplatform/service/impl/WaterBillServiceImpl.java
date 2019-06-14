package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.WaterBill;
import com.scut.p2ploanplatform.enums.PayModeEnum;
import com.scut.p2ploanplatform.service.WaterBillService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by zonghang
 * Date 2019/6/14 15:52
 */
@Service
public class WaterBillServiceImpl implements WaterBillService {

    @Override
    public int addNewWaterBill(String payeeId, String payerId, BigDecimal amount, PayModeEnum payModeEnum, Date time) {
        return 0;
    }

    @Override
    public List<WaterBill> findUserPayRecord(String userId) {
        return null;
    }

    @Override
    public List<WaterBill> findUserIncomeRecord(String userId) {
        return null;
    }

    @Override
    public List<WaterBill> findWaterBillByMode(PayModeEnum payModeEnum) {
        return null;
    }
}
