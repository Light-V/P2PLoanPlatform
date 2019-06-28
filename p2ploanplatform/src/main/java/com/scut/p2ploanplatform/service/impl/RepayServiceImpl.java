package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.RepayPlanDao;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import com.scut.p2ploanplatform.service.RepayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

@Service
@Slf4j
public class RepayServiceImpl implements RepayService {
    private RepayPlanDao repayPlanDao;

    @Autowired
    public void setRepayPlanDao(RepayPlanDao repayPlanDao) {
        this.repayPlanDao = repayPlanDao;
    }

    private static Date getDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("UTC"));
        c.setTime(date);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    @Override
    public RepayPlan insertPlan(Integer purchaseId, Date repayDate, BigDecimal amount) throws SQLException, IllegalArgumentException {
        if (purchaseId == null || purchaseId <= 0)
            throw new IllegalArgumentException("invalid purchaseId, should be non null and positive");
        if (repayDate == null)
            throw new IllegalArgumentException("invalid repayDate, should be non null");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("invalid amount, should be non null and positive");

        repayDate = getDate(repayDate);
        RepayPlan plan = new RepayPlan();
        plan.setRepayDate(repayDate);
        plan.setAmount(amount);
        plan.setPlanId(UUID.randomUUID().toString().replace("-", ""));
        plan.setPurchaseId(purchaseId);
        if (repayDate.before(new Date())) {
            log.warn("Trying to insert an overdue repay plan, check your code");
            plan.setStatus(RepayPlanStatus.OVERDUE.getStatus());
        } else {
            plan.setStatus(RepayPlanStatus.SCHEDULED.getStatus());
        }

        try {
            repayPlanDao.insertPlan(plan);
        } catch (Exception e) {
            throw new SQLException(e);
        }

        return plan;
    }

    @Override
    public List<RepayPlan> findPlanByPurchaseId(Integer id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0)
            throw new IllegalArgumentException("invalid purchaseId, should be non null and positive");

        try {
            return repayPlanDao.findPlanByPurchaseId(id);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public RepayPlan findPlanById(String id) throws SQLException, IllegalArgumentException {
        if (id == null || id.length() != 32)
            throw new IllegalArgumentException("invalid planId, should be non null and has length of 32");

        try {
            return repayPlanDao.findPlanByPlanId(id);
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRepayPlan(String id, RepayPlanStatus status, Date realRepayDate) throws SQLException, IllegalArgumentException {
        if (id == null || id.length() != 32)
            throw new IllegalArgumentException("invalid planId, should be non null and has length of 32");
        if (status == null)
            throw new IllegalArgumentException("status could not be null");
        if (realRepayDate != null && (status != RepayPlanStatus.SUCCEEDED && status != RepayPlanStatus.OVERDUE_SUCCEEDED))
            throw new IllegalArgumentException("invalid argument: realRepayDate and status, status must be SUCCEEDED if realRepayDate is set");
        try {
            RepayPlan plan = repayPlanDao.findPlanByPlanId(id);

            // optional: add expire check here
            plan.setStatus(status.getStatus());
            plan.setRealRepayDate(realRepayDate);
            repayPlanDao.updatePlan(plan);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

}
