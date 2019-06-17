package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.List;

/**
 * @author FatCat
 */
public class LoanApplicationServiceImpl implements LoanApplicationService{
    @Autowired
    private LoanApplicationService loanApplicationService;

    @Override
    public Boolean addApplication(LoanApplication loanApplication) throws SQLException {
        return null;
    }

    @Override
    public Boolean changeStatusById(Integer id, LoanStatus loanStatus) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public Boolean modifyApplication(LoanApplication loanApplication) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public Boolean deleteApplicationById(Integer id) throws SQLException {
        return null;
    }

    @Override
    public List<LoanApplication> showApplicationByBorrowerId(String borrowerId) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public List<LoanApplication> showApplicationByGuarantorId(String guarantorId) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public List<LoanApplication> showApplicationByBorrowerId(String borrowerId, LoanStatus loanStatus) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public List<LoanApplication> showApplicationByGuarantorId(String guarantorId, LoanStatus loanStatus) throws SQLException, IllegalArgumentException {
        return null;
    }

    @Override
    public List<LoanApplication> showApplicationReviewedPassed() throws SQLException, IllegalArgumentException {
        return null;
    }
}
