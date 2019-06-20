package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.dao.WaterBillDao;
import com.scut.p2ploanplatform.entity.WaterBill;
import com.scut.p2ploanplatform.enums.PayModeEnum;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.CustomException;
import com.scut.p2ploanplatform.service.WaterBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by zonghang
 * Date 2019/6/14 15:52
 */
@Service
public class WaterBillServiceImpl implements WaterBillService {

    @Autowired
    private WaterBillDao waterBillDao;

    private final String ORDER_BY_TIME_DESC = "time desc";

    @Override
    public int addNewWaterBill(String payeeId, String payerId, BigDecimal amount, PayModeEnum payModeEnum, Date time) {
        WaterBill waterBill = new WaterBill();
        waterBill.setWaterBillId(UUID.randomUUID().toString().replace("-", ""));
        waterBill.setPayeeId(payeeId);
        waterBill.setPayerId(payerId);
        waterBill.setAmount(amount);
        waterBill.setMode(payModeEnum.getCode());
        waterBill.setTime(time);
        return waterBillDao.insert(waterBill);
    }

    @Override
    public PageInfo<WaterBill> findUserPayRecord(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, ORDER_BY_TIME_DESC);
        List<WaterBill> waterBillList = waterBillDao.findByPayerId(userId);
        return new PageInfo<>(waterBillList);
    }

    @Override
    public PageInfo<WaterBill> findUserIncomeRecord(String userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, ORDER_BY_TIME_DESC);
        List<WaterBill> waterBillList = waterBillDao.findByPayeeId(userId);
        return new PageInfo<>(waterBillList);
    }

    @Override
    public List<WaterBill> findWaterBillByModeAndTime(PayModeEnum payModeEnum, String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate, endDate;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
        } catch (ParseException e) {
            throw new CustomException(ResultEnum.DATE_ERROR);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(endDate);
        calendar.add(Calendar.HOUR, 24);
        calendar.add(Calendar.SECOND, -1);
        endDate = calendar.getTime();
        return waterBillDao.findByModeAndTime(payModeEnum.getCode(), startDate, endDate);
    }
}
