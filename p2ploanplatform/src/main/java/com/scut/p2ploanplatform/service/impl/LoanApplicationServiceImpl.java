package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.dao.LoanApplicationDao;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

/**
 * @author FatCat
 */

//todo: notice
@Service
public class LoanApplicationServiceImpl implements LoanApplicationService{

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Override
    public Boolean addApplication(LoanApplication loanApplication) throws SQLException {
        try {
            return loanApplicationDao.addApplication(loanApplication);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Boolean reviewPass(Integer id, String guarantorId) throws SQLException, IllegalArgumentException {
        LoanStatus loanStatus = LoanStatus.REVIEWED_PASSED;
        return changeStatusById(id,guarantorId, loanStatus);
    }

    @Override
    public Boolean reviewReject(Integer id, String guarantorId) throws SQLException, IllegalArgumentException {
        LoanStatus loanStatus = LoanStatus.REVIEWED_REJECTED;
        return changeStatusById(id, guarantorId, loanStatus);
    }

    @Override
    public Boolean subscribe(Integer id) throws SQLException, IllegalArgumentException {
        LoanStatus loanStatus = LoanStatus.SUBSCRIBED;
        return changeStatusById(id,loanStatus);
    }

    @Override
    public Boolean expire(Integer id) throws SQLException, IllegalArgumentException {
        LoanStatus loanStatus = LoanStatus.EXPIRED;
        return changeStatusById(id,loanStatus);
    }

    @Transactional
    public Boolean changeStatusById(Integer id, LoanStatus status) throws SQLException, IllegalArgumentException {
        try {
            LoanApplication loanApplication = loanApplicationDao.getApplicationById(id);
            if(loanApplication == null){
                throw new IllegalArgumentException("invalid applicationId, non loan application found with this applicationId");
            }
            loanApplication.setStatus(status.getStatus());
            return loanApplicationDao.updateApplication(loanApplication);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Transactional
    public Boolean changeStatusById(Integer id,String guarantorId, LoanStatus status) throws SQLException, IllegalArgumentException {
        try {
            LoanApplication loanApplication = loanApplicationDao.getApplicationById(id);
            if(loanApplication == null){
                throw new IllegalArgumentException("invalid applicationId, non loan application found with this applicationId");
            }
            loanApplication.setStatus(status.getStatus());
            loanApplication.setGuarantorId(guarantorId);
            return loanApplicationDao.updateApplication(loanApplication);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Boolean modifyApplication(LoanApplication loanApplication) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public Boolean deleteApplicationById(Integer id) throws SQLException {
        if (id == null)
            throw new IllegalArgumentException("invalid applicationId, should be non null");

        try {
            return loanApplicationDao.deleteApplicationById(id);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public LoanApplication getApplicationById(Integer id) throws SQLException, IllegalArgumentException {
        if (id == null)
            throw new IllegalArgumentException("invalid applicationId, should be non null");

        try {
            return loanApplicationDao.getApplicationById(id);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public PageInfo<LoanApplication> getAllApplication(Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getAllApplication();
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getApplicationByBorrowerId(String borrowerId, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        if (borrowerId == null || borrowerId.length() != 12)
            throw new IllegalArgumentException("invalid guarantorId, should be non null and has length of 12");
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getApplicationByBorrowerId(borrowerId);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getApplicationByGuarantorId(String guarantorId, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        if (guarantorId == null || guarantorId.length() != 12)
            throw new IllegalArgumentException("invalid guarantorId, should be non null and has length of 12");
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getApplicationByGuarantorId(guarantorId);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getApplicationByBorrowerId(String borrowerId, Integer status, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        if (borrowerId == null || borrowerId.length() != 12)
            throw new IllegalArgumentException("invalid borrowerId, should be non null and has length of 12");
        if (status == null )
            throw new IllegalArgumentException("invalid loan status, should be non null");

        LoanStatus loanStatus;

        try{
            loanStatus = LoanStatus.values()[status];
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getApplicationByBorrowerIdAndStatus(borrowerId,status);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getApplicationByGuarantorId(String guarantorId, Integer status, Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        if (guarantorId == null || guarantorId.length() != 12)
            throw new IllegalArgumentException("invalid guarantorId, should be non null and has length of 12");
        if (status == null )
            throw new IllegalArgumentException("invalid loan status, should be non null");

        LoanStatus loanStatus;

        try{
            loanStatus = LoanStatus.values()[status];
        }
        catch (Exception e){
            throw new IllegalArgumentException(e);
        }

        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getApplicationByGuarantorIdAndStatus(guarantorId, status);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getApplicationReviewedPassed(Integer pageNum, Integer pageSize) throws SQLException{
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getApplicationReviewedPassed();
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getApplicationUnReviewed(Integer pageNum, Integer pageSize) throws SQLException {
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getApplicationUnReviewed();
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }
}
