package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.RepayPlanDao;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.enums.RepayPlanStatus;
import com.scut.p2ploanplatform.service.PurchaseService;
import com.scut.p2ploanplatform.service.RepayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class RepayServiceImpl implements RepayService {
    private Logger logger = LoggerFactory.getLogger(RepayServiceImpl.class);
    private boolean isColdStart = true;

    private RepayPlanDao repayPlanDao;
    private PurchaseService purchaseService;

    @Autowired
    public void setRepayPlanDao(RepayPlanDao repayPlanDao) {
        this.repayPlanDao = repayPlanDao;
    }

    @Autowired
    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public RepayServiceImpl() throws Exception {
        RepayAutoTrigger trigger = new RepayAutoTrigger(this, getClass().getDeclaredMethod("doRepay"));
        Thread thread = new Thread(trigger);
        thread.setDaemon(true);
        thread.setName("RepayThread");
        thread.start();
    }


    @Override
    public RepayPlan insertPlan(Integer purchaseId, Date repayDate, BigDecimal amount) throws SQLException, IllegalArgumentException {
        if (purchaseId == null || purchaseId <= 0)
            throw new IllegalArgumentException("invalid purchaseId, should be non null and positive");
        if (repayDate == null)
            throw new IllegalArgumentException("invalid repayDate, should be non null");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("invalid amount, should be non null and positive");

        RepayPlan plan = new RepayPlan();
        plan.setRepayDate(repayDate);
        plan.setAmount(amount);
        plan.setPlanId(UUID.randomUUID().toString().replace("-", ""));
        plan.setPurchaseId(purchaseId);
        if (repayDate.before(new Date())) {
            logger.warn("Trying to insert an overdue repay plan, check your code");
            plan.setStatus(RepayPlanStatus.OVERDUE.getStatus());
        } else {
            plan.setStatus(RepayPlanStatus.SCHEDULED.getStatus());
        }

        try {
            repayPlanDao.insertPlan(plan);
        }
        catch (Exception e) {
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
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public RepayPlan findPlanById(String id) throws SQLException, IllegalArgumentException {
        if (id == null || id.length() != 32)
            throw new IllegalArgumentException("invalid planId, should be non null and has length of 32");

        try {
            return repayPlanDao.findPlanByPlanId(id);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void updateRepayPlan(String id, RepayPlanStatus status, Date realRepayDate) throws SQLException, IllegalArgumentException {
        if (id == null || id.length() != 32)
            throw new IllegalArgumentException("invalid planId, should be non null and has length of 32");
        try {
            RepayPlan plan = repayPlanDao.findPlanByPlanId(id);
            if (status == null) {
                if (realRepayDate != null)
                    status = RepayPlanStatus.SUCCEEDED;
                else {
                    if (plan.getRepayDate().before(new Date()))
                        status = RepayPlanStatus.OVERDUE;
                    else
                        status = RepayPlanStatus.SCHEDULED;
                }
            }
            if (realRepayDate != null && status != RepayPlanStatus.SUCCEEDED)
                throw new IllegalArgumentException("invalid argument: realRepayDate and status, status must be SUCCEEDED if realRepayDate is set");

            // optional: add expire check here
            plan.setStatus(status.getStatus());
            plan.setRealRepayDate(realRepayDate);
            repayPlanDao.updatePlan(plan);
        }
        catch (IllegalArgumentException e) {
            throw e;
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public synchronized void doRepay() {
        // waiting autowired value set
        while (repayPlanDao == null || purchaseService == null) {
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException ignore) {
                return;
            }
        }
        logger.info("Starting daily repay process");

        if (isColdStart) {
            isColdStart = false;
            logger.info("Updating plan status (from cold start)");
            int rowsAffected = repayPlanDao.updatePlanStatus();
            logger.info("Completed query, " + rowsAffected + " rows affected");
        }

        List<RepayPlan> plans = repayPlanDao.findAllUnpaidPlan();
        logger.info("Completed query, got " + plans.size() + " results");

        for (RepayPlan plan : plans) {
            // 还款流程

            // get purchase from id
            Purchase purchase = purchaseService.showPurchaseById(plan.getPurchaseId());

            // get user info from uid
            //User borrower =
        }
    }
}


@SuppressWarnings("WeakerAccess")
class RepayAutoTrigger implements Runnable {
    private Logger logger = LoggerFactory.getLogger(RepayAutoTrigger.class);
    // 这里定义了每天触发还款任务的时间
    // todo: modify this hard-coded schedule time to global config in Iteration 2
    public static final int REPAY_HOUR = 8;
    public static final int REPAY_MINUTE = 0;
    public static final int REPAY_SECOND = 0;

    private Method callbackMethod;
    private Object invokeObject;

    RepayAutoTrigger(Object invokeObject, Method callbackMethod) {
        this.invokeObject = invokeObject;
        this.callbackMethod = callbackMethod;
    }

    @Override
    public void run() {
        try {
            logger.info("Started repay trigger thread");
            long repayTimeOfDay = (REPAY_SECOND + (60 * (60 * REPAY_HOUR + REPAY_MINUTE))) * 1000;
            long nextDateTime = (new Date().getTime() + 86399999) / 86400000;
            long nextRepayTimestamp = nextDateTime * 86400000 + repayTimeOfDay;

            //noinspection InfiniteLoopStatement
            while (true) {
                callbackMethod.invoke(invokeObject);

                nextRepayTimestamp += 86400000;
                long sleepTimeForNextDay = (nextRepayTimestamp - new Date().getTime());
                if (sleepTimeForNextDay > 0)
                    Thread.sleep(sleepTimeForNextDay);
            }
        }catch (Exception e) {
            logger.error(e.toString());

            // retry
            Thread thread = new Thread(() -> {
                try {
                    logger.warn("Unhandled exception caught in auto trigger thread, restarting in 1 hours");
                    Thread.sleep(3600000);
                    Thread restartThread = new Thread(new RepayAutoTrigger(this.invokeObject, this.callbackMethod));
                    restartThread.setDaemon(true);
                    restartThread.setName("RepayThread");
                    restartThread.start();
                }
                catch (InterruptedException ignore) {}
            });

            thread.setDaemon(true);
            thread.setName("RepayThread");
            thread.start();
        }
    }
}