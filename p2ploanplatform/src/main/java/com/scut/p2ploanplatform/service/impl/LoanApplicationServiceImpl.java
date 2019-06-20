package com.scut.p2ploanplatform.service.impl;

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
    public Boolean reviewPass(Integer id) throws SQLException, IllegalArgumentException {
        LoanStatus loanStatus = LoanStatus.REVIEWED_PASSED;
        return changeStatusById(id,loanStatus);
    }

    @Override
    public Boolean reviewReject(Integer id) throws SQLException, IllegalArgumentException {
        LoanStatus loanStatus = LoanStatus.REVIEWED_REJECTED;
        return changeStatusById(id,loanStatus);
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
            LoanApplication loanApplication = loanApplicationDao.showApplicationById(id);
            loanApplication.setStatus(status.getStatus());
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
    public LoanApplication showApplicationById(Integer id) throws SQLException, IllegalArgumentException {
        if (id == null)
            throw new IllegalArgumentException("invalid applicationId, should be non null");

        try {
            return loanApplicationDao.showApplicationById(id);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<LoanApplication> showApplicationByBorrowerId(String borrowerId) throws SQLException, IllegalArgumentException {
        if (borrowerId == null || borrowerId.length() != 12)
            throw new IllegalArgumentException("invalid guarantorId, should be non null and has length of 12");

        try {
            return loanApplicationDao.showApplicationByBorrowerId(borrowerId);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<LoanApplication> showApplicationByGuarantorId(String guarantorId) throws SQLException, IllegalArgumentException {
        if (guarantorId == null || guarantorId.length() != 12)
            throw new IllegalArgumentException("invalid guarantorId, should be non null and has length of 12");

        try {
            return loanApplicationDao.showApplicationByGuarantorId(guarantorId);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<LoanApplication> showApplicationByBorrowerId(String borrowerId, Integer status) throws SQLException, IllegalArgumentException {
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

        try {
            return loanApplicationDao.showApplicationByBorrowerIdAndStatus(borrowerId,loanStatus.getStatus());
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<LoanApplication> showApplicationByGuarantorId(String guarantorId, Integer status) throws SQLException, IllegalArgumentException {
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

        try {
            return loanApplicationDao.showApplicationByGuarantorIdAndStatus(guarantorId,loanStatus.getStatus());
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<LoanApplication> showApplicationReviewedPassed() throws SQLException{
        try {
            return loanApplicationDao.showApplicationReviewedPassed();
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
    }
}
