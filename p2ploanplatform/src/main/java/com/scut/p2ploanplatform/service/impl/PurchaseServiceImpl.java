package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.PurchaseDao;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.LoanStatusException;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
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


    @Override
    public Purchase subscribed(String investorId, Integer applicationId) throws SQLException, IllegalArgumentException, LoanStatusException {
        try {
            LoanApplication application = applicationService.getApplicationById(applicationId);
            if(application == null){
                throw new IllegalArgumentException("invalid applicationId, non loan application found with this applicationId");
            }
            if(!application.getStatus().equals(LoanStatus.REVIEWED_PASSED.getStatus())){
                throw new LoanStatusException(ResultEnum.APPLICATION_NOT_PASS_REVIEWED);
            }
            Purchase purchase = new Purchase(application);
            purchase.setInvestorId(investorId);
            if(purchaseDao.createPurchaseItem(purchase)){
                applicationService.subscribe(applicationId);
                return purchase;
            }else{
                throw new SQLException("数据库操作失败");
            }
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Boolean purchaseOverdue(Integer purchaseId) throws SQLException, IllegalArgumentException {
        try {
            if(purchaseDao.updatePurchaseStatus(purchaseId,LoanStatus.OVERDUE.getStatus())){
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
    public List<Purchase> showAllPurchase() throws SQLException {
        try{
            return purchaseDao.showAllPurchase();
        }
        catch (Exception e){
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
    public List<Purchase> showPurchaseByBorrowerId(String borrowerID) throws SQLException, IllegalArgumentException {
        try{
            return purchaseDao.getPurchaseByBorrowerId(borrowerID);
        }
        catch (Exception e){
            throw new SQLException(e);
        }
    }

    @Override
    public List<Purchase> showPurchaseByInvestorId(String investorID) throws SQLException, IllegalArgumentException {
        try{
            return purchaseDao.getPurchaseByInvestorId(investorID);
        }
        catch (Exception e){
            throw new SQLException(e);
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
}
