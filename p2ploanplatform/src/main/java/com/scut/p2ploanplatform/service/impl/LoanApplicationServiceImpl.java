package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.dao.LoanApplicationDao;
import com.scut.p2ploanplatform.dao.PurchaseDao;
import com.scut.p2ploanplatform.dto.UserHistory;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.LoanStatusException;
import com.scut.p2ploanplatform.service.LoanApplicationService;
import com.scut.p2ploanplatform.service.PurchaseService;
import com.scut.p2ploanplatform.service.UserService;
import com.scut.p2ploanplatform.utils.AutoTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.*;

/**
 * @author FatCat
 */

//todo: notice
@Service
@Slf4j
public class LoanApplicationServiceImpl implements LoanApplicationService{

    @Autowired
    private LoanApplicationDao loanApplicationDao;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private NoticeServiceImpl noticeService;

    public LoanApplicationServiceImpl() throws Exception {
        new AutoTrigger(getClass().getDeclaredMethod("manualExpire"), this, 16, 0, 0, true);
    }

    @Override
    public Boolean addApplication(LoanApplication loanApplication) throws SQLException {
        if( loanApplicationDao.addApplication(loanApplication)) {
            noticeService.sendNotice(loanApplication.getBorrowerId(), "贷款申请提交成功", "您的贷款申请已提交成功，正在等待审核。");
            return true;
        }
        else {
            throw new SQLException("数据库操作失败");
        }
    }

    @Override
    public Boolean reviewPass(Integer id, String guarantorId) throws SQLException, IllegalArgumentException {
        LoanApplication application = getApplicationById(id);
        if(application.getStatus().equals(LoanStatus.UNREVIEWED.getStatus())){
            LoanStatus loanStatus = LoanStatus.REVIEWED_PASSED;
            return changeStatusById(id,guarantorId, loanStatus);
        }else {
            throw new LoanStatusException(ResultEnum.ILLEGAL_OPERATION);
        }
    }

    @Override
    public Boolean reviewReject(Integer id, String guarantorId) throws SQLException, IllegalArgumentException {
        LoanApplication application = getApplicationById(id);
        if(application.getStatus().equals(LoanStatus.UNREVIEWED.getStatus())){
            LoanStatus loanStatus = LoanStatus.REVIEWED_REJECTED;
            return changeStatusById(id, guarantorId, loanStatus);
        }else {
            throw new LoanStatusException(ResultEnum.ILLEGAL_OPERATION);
        }
    }

    @Override
    public Boolean subscribe(Integer id) throws SQLException, IllegalArgumentException {
        LoanApplication application = getApplicationById(id);
        if(application.getStatus().equals(LoanStatus.REVIEWED_PASSED.getStatus())){
            LoanStatus loanStatus = LoanStatus.SUBSCRIBED;
            return changeStatusById(id,loanStatus);
        }else {
            throw new LoanStatusException(ResultEnum.APPLICATION_NOT_PASS_REVIEWED);
        }
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
        LoanApplication application;
        try {
            application = loanApplicationDao.getApplicationById(id);
            application.setBorrowerName(userService.findUser(application.getBorrowerId()).getName());
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return application;
    }

    @Override
    public PageInfo<LoanApplication> getAllApplication(Integer pageNum, Integer pageSize) throws SQLException, IllegalArgumentException {
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getAllApplication();
            applicationList =setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
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
            applicationList =setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
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
            applicationList =setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
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
            applicationList =setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
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
            applicationList =setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
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
            applicationList = setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
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
            applicationList =setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getAll012Application(Integer pageNum, Integer pageSize) throws SQLException {
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.get012Application();
            applicationList =setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void manualExpire() {
        log.info("贷款申请认购期限审查");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        List<LoanApplication> applicationList = loanApplicationDao.getApplicationReviewedPassedExpired(calendar.getTime());
        for (LoanApplication loanApplication : applicationList) {
            loanApplication.setStatus(LoanStatus.EXPIRED.getStatus());
            loanApplicationDao.updateApplication(loanApplication);
            noticeService.sendNotice(loanApplication.getBorrowerId(), "贷款申请已过期",
                    "你的贷款申请由于长时间没被认购，已过期");
        }
    }

    private List<LoanApplication> setUserName(List<LoanApplication> list) throws Exception
    {
        for(LoanApplication application:list){
            application.setBorrowerName(userService.findUser(application.getBorrowerId()).getName());
//            if(application.getStatus().equals(LoanStatus.UNREVIEWED.getStatus()))
//            application.setGuarantorName(userService.findUser(application.getGuarantorName()).getName());
        }
        return list;
    }

    @Override
    public PageInfo<LoanApplication> getApplicationReviewedRejected(Integer pageNum, Integer pageSize) throws SQLException {
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getApplicationReviewedRejected();
            applicationList = setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getApplicationReviewExpired(Integer pageNum, Integer pageSize) throws SQLException {
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            applicationList = loanApplicationDao.getApplicationReviewExpired(calendar.getTime());
            applicationList = setUserName(applicationList);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<LoanApplication> getOverdueApplicationById(Integer pageNum, Integer pageSize, String userId) throws SQLException {
        PageHelper.startPage(pageNum, pageSize);
        List<LoanApplication> applicationList;
        try {
            applicationList = loanApplicationDao.getOverdueApplicationById(userId);
            applicationList = setUserName(applicationList);
            applicationList.sort(Comparator.comparing(LoanApplication::getUpdateTime));
            Collections.reverse(applicationList);
        }
        catch (Exception e) {
            throw new SQLException(e);
        }
        return new PageInfo<>(applicationList);
    }

    @Override
    public PageInfo<UserHistory> getUserHistory(Integer pageNum, Integer pageSize, String userId) throws SQLException {
        PageHelper.startPage(pageNum, pageSize);
        List<UserHistory> userHistoryList;
        List<Purchase> purchaseList;
        try {
            purchaseList = purchaseDao.getPurchaseByBorrowerId(userId);
            PageInfo purchasePageInfo = new PageInfo<>(purchaseList);
            PageInfo<UserHistory> userHistoryPageInfo = new PageInfo<>();
            BeanUtils.copyProperties(purchasePageInfo, userHistoryPageInfo);
            userHistoryList = setUserNameAndConvert(purchaseList);
            userHistoryPageInfo.setList(userHistoryList);
            return userHistoryPageInfo;
        }
        catch (Exception e){
            throw new SQLException(e);
        }
    }


    private List<UserHistory> setUserNameAndConvert(List<Purchase> list) throws Exception
    {
        ArrayList<UserHistory> userHistoryList = new ArrayList<>();
        for(Purchase purchase:list){
            userHistoryList.add(new UserHistory(purchase));
        }

        return userHistoryList;
    }
}
