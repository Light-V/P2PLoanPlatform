package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.dao.PurchaseDao;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.LoanStatusException;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.service.PurchaseService;
import com.scut.p2ploanplatform.service.RepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
/**
 * @author FatCat
 */
@Service
//todo: notice
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private LoanApplicationService applicationService;

    @Autowired
    private RepayService repayService;

    @Override
    @Transactional
    public Purchase subscribed(String investorId, Integer applicationId) throws SQLException, IllegalArgumentException, LoanStatusException {
        LoanApplication application = applicationService.getApplicationById(applicationId);
        if(application == null){
            throw new IllegalArgumentException("不存在的借款申请：未找到该借款申请");
        }
        if(!application.getStatus().equals(LoanStatus.REVIEWED_PASSED.getStatus())){
            throw new LoanStatusException(ResultEnum.APPLICATION_NOT_PASS_REVIEWED);
        }
        Purchase purchase = new Purchase(application);
        purchase.setInvestorId(investorId);
        if(purchaseDao.createPurchaseItem(purchase)){
            applicationService.subscribe(applicationId);
            setRepayPlan(purchase);
            purchase.setRepayPlans(repayService.findPlanByPurchaseId(purchase.getPurchaseId()));
            return purchase;
        }else{
            throw new SQLException("数据库操作失败");
        }
    }

    private void setRepayPlan(Purchase purchase){
//        Date repaydate = purchase.getCreateTime();
//        repayService.insertPlan(purchase.getPurchaseId(),)
    }
    @Override
    public Boolean purchaseOverdue(Integer purchaseId) throws SQLException, IllegalArgumentException {
        if(purchaseDao.updatePurchaseStatus(purchaseId,LoanStatus.OVERDUE.getStatus())){
            return true;
        }else {
            throw new IllegalArgumentException("invalid purchaseId, non loan purchase found with this purchaseId");
        }
    }

    @Override
    public Boolean accomplishPurchase(Integer purchaseId) throws SQLException, IllegalArgumentException {
        try {
            if(purchaseDao.updatePurchaseStatus(purchaseId,LoanStatus.FINISHED.getStatus())){
                return true;
            }else {
                throw new IllegalArgumentException("invalid purchaseId, non loan purchase found with this purchaseId");
            }
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Purchase showPurchaseById(Integer purchaseId) throws SQLException, IllegalArgumentException {
        try{
            return purchaseDao.getPurchaseByPurchaseId(purchaseId);
        }
        catch (Exception e){
            throw new SQLException(e);
        }
    }

    @Override
    public PageInfo<Purchase> showAllPurchase(Integer pageNum, Integer pageSize) throws SQLException {
        PageHelper.startPage(pageNum, pageSize);
        List<Purchase> purchaseList;
        try{
            purchaseList = purchaseDao.showAllPurchase();
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return new PageInfo<>(purchaseList);
    }

    @Override
    public PageInfo<Purchase> showPurchaseByBorrowerId(String borrowerID, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        PageHelper.startPage(pageNum, pageSize);
        List<Purchase> purchaseList;
        try{
            purchaseList = purchaseDao.getPurchaseByBorrowerId(borrowerID);
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return new PageInfo<>(purchaseList);
    }

    @Override
    public PageInfo<Purchase> showPurchaseByBorrowerId(String borrowerID, LoanStatus status, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        PageHelper.startPage(pageNum, pageSize);
        List<Purchase> purchaseList;
        try{
            purchaseList = purchaseDao.getPurchaseByBorrowerIdAndStatus(borrowerID, status.getStatus());
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return new PageInfo<>(purchaseList);
    }

    @Override
    public PageInfo<Purchase> showPurchaseByInvestorId(String investorID, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        PageHelper.startPage(pageNum, pageSize);
        List<Purchase> purchaseList;
        try{
            purchaseList = purchaseDao.getPurchaseByInvestorId(investorID);
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return new PageInfo<>(purchaseList);
    }

    @Override
    public PageInfo<Purchase> showPurchaseByInvestorId(String investorID, LoanStatus status, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        PageHelper.startPage(pageNum, pageSize);
        List<Purchase> purchaseList;
        try{
            purchaseList = purchaseDao.getPurchaseByInvestorIdAndStatus(investorID,status.getStatus());
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return new PageInfo<>(purchaseList);
    }


}
