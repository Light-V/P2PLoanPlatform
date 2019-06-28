package com.scut.p2ploanplatform.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.scut.p2ploanplatform.dao.PurchaseDao;
import com.scut.p2ploanplatform.entity.LoanApplication;
import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.entity.RepayPlan;
import com.scut.p2ploanplatform.entity.User;
import com.scut.p2ploanplatform.enums.LoanStatus;
import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.CustomException;
import com.scut.p2ploanplatform.exception.LoanStatusException;
import com.scut.p2ploanplatform.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
/**
 * @author FatCat
 */
@Service
//todo: notice
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;
    private final UserService userService;
    private final LoanApplicationService applicationService;
    private final RepayService repayService;
    private final P2pAccountService p2pAccountService;

    @Autowired
    public PurchaseServiceImpl(PurchaseDao purchaseDao, UserService userService, LoanApplicationService applicationService, RepayService repayService, P2pAccountService p2pAccountService) {
        this.purchaseDao = purchaseDao;
        this.userService = userService;
        this.applicationService = applicationService;
        this.repayService = repayService;
        this.p2pAccountService = p2pAccountService;
    }

    @Override
    @Transactional
    public Purchase subscribed(String investorId, Integer applicationId) throws SQLException, IllegalArgumentException, LoanStatusException {
        //获取借款申请信息
        LoanApplication application = applicationService.getApplicationById(applicationId);

        //检查借款申请
        if(application == null){
            throw new IllegalArgumentException("不存在的借款申请：未找到该借款申请");
        }
        if(!application.getStatus().equals(LoanStatus.REVIEWED_PASSED.getStatus())){
            throw new LoanStatusException(ResultEnum.APPLICATION_NOT_PASS_REVIEWED);
        }

        //获取用户信息
        User borrower = userService.findUser(application.getBorrowerId());
        User investor = userService.findUser(investorId);

        //执行转账
        if(!p2pAccountService.transfer(investor.getThirdPartyId(),borrower.getThirdPartyId(), application.getAmount())){
            throw new CustomException("操作失败：账号余额不足", ResultEnum.ILLEGAL_OPERATION.getCode());
        }

        //更新数据库中的订单和还款计划表单
        Purchase purchase = new Purchase(application);
        purchase.setInvestorId(investorId);
        purchase.setPurchaseTime(Calendar.getInstance().getTime());
        if(purchaseDao.createPurchaseItem(purchase)){
            applicationService.subscribe(applicationId);    //更新借款申请状态
            setRepayPlan(purchase);                         //初始化还款计划
            List<RepayPlan> repayPlans= repayService.findPlanByPurchaseId(purchase.getPurchaseId());
            repayPlans.sort(Comparator.comparing(RepayPlan::getRepayDate));//使还款计划按还款时间排序
            purchase.setRepayPlans(repayPlans);
            return purchase;
        }else{
            throw new SQLException("数据库操作失败");
        }
    }

    @Transactional
    protected void setRepayPlan(Purchase purchase)throws SQLException{
        Date nextRepayDate = purchase.getPurchaseTime();
        Integer months = purchase.getLoanMonth();
        BigDecimal totalAmount = purchase.getAmount();
        BigDecimal perAmout = totalAmount.divide(new BigDecimal(months),8,BigDecimal.ROUND_HALF_UP);
        for(int i = 0;i<months;i++){
            Calendar nextRepay = Calendar.getInstance();
            nextRepay.setTime(nextRepayDate);
            nextRepay.add(Calendar.MONTH, 1);
            nextRepayDate = nextRepay.getTime();
            repayService.insertPlan(purchase.getPurchaseId(),nextRepayDate,perAmout);
        }
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
        Purchase purchase = null;
        try{
            purchase = purchaseDao.getPurchaseByPurchaseId(purchaseId);
            List<RepayPlan> repayPlans = repayService.findPlanByPurchaseId(purchaseId);
            repayPlans.sort(Comparator.comparing(RepayPlan::getRepayDate));
            purchase.setRepayPlans(repayPlans);
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return purchase;
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
