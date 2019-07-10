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
import com.scut.p2ploanplatform.utils.AutoTrigger;
import com.scut.p2ploanplatform.utils.ThirdPartyOperationInterface;
import com.scut.p2ploanplatform.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
//todo: notice
public class PurchaseServiceImpl implements PurchaseService {

    private final PurchaseDao purchaseDao;
    private final UserService userService;
    private final LoanApplicationService applicationService;
    private final RepayService repayService;
    private final P2pAccountService p2pAccountService;
    private final NoticeService noticeService;
    private final GuarantorService guarantorService;

    @Autowired
    public PurchaseServiceImpl(PurchaseDao purchaseDao, UserService userService,
                               LoanApplicationService applicationService, RepayService repayService,
                               P2pAccountService p2pAccountService, NoticeService noticeService,
                               GuarantorService guarantorService) throws Exception{
        this.purchaseDao = purchaseDao;
        this.userService = userService;
        this.applicationService = applicationService;
        this.repayService = repayService;
        this.p2pAccountService = p2pAccountService;
        this.noticeService = noticeService;
        this.guarantorService = guarantorService;
        new AutoTrigger(getClass().getDeclaredMethod("manualFinish"), this, 2, 0, 0, true);

    }

    @Override
    @Transactional
    public Purchase subscribed(String investorId, Integer applicationId,String password) throws Exception{
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
        ResultVo purchaseResult;
        purchaseResult = ThirdPartyOperationInterface.purchase(investor.getThirdPartyId(),
                borrower.getThirdPartyId(),application.getAmount(),password);
        if (purchaseResult.getCode()!=0){
            throw new RuntimeException(purchaseResult.getMsg());
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
        }else{
            throw new SQLException("数据库操作失败");
        }

        //通知
        String borrowerNotice = "尊敬的 %s：\n\t您编号为 %06d 的借款申请已被认购，订单编号为 %06d，请及时检查款项是否到账，并前往订单详情页确认还款计划，及时还款。";
        noticeService.sendNotice(borrower.getUserId(), "借款申请被认购", String.format(borrowerNotice, borrower.getName(),application.getApplicationId(),purchase.getPurchaseId()));
        String investorNotice = "尊敬的 %s：\n\t您已成功认购编号为 %06d 的借款申请，订单编号为 %06d，可以前往订单详情页确认还款计划。";
        noticeService.sendNotice(investorId, "借款申请被认购", String.format(investorNotice, investor.getName(),application.getApplicationId(),purchase.getPurchaseId()));

        return purchase;
    }

    @Transactional
    protected void setRepayPlan(Purchase purchase)throws SQLException{
        Date nextRepayDate = purchase.getPurchaseTime();
        Integer months = purchase.getLoanMonth();
        BigDecimal amount = purchase.getAmount();
        BigDecimal interestRate = purchase.getInterestRate();
        BigDecimal totalAmount = amount.multiply(interestRate).add(amount);
        BigDecimal perAmount = totalAmount.divide(new BigDecimal(months),8,BigDecimal.ROUND_HALF_UP);
        for(int i = 0;i<months;i++){
            Calendar nextRepay = Calendar.getInstance();
            nextRepay.setTime(nextRepayDate);
            nextRepay.add(Calendar.MONTH, 1);
            nextRepayDate = nextRepay.getTime();
            repayService.insertPlan(purchase.getPurchaseId(),nextRepayDate,perAmount);
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
        Purchase purchase;
        try{
            purchase = purchaseDao.getPurchaseByPurchaseId(purchaseId);
            List<RepayPlan> repayPlans = repayService.findPlanByPurchaseId(purchaseId);
            repayPlans.sort(Comparator.comparing(RepayPlan::getRepayDate));
            purchase.setRepayPlans(repayPlans);
            purchase.setBorrowerName(userService.findUser(purchase.getBorrowerId()).getName());
            purchase.setInvestorName(userService.findUser(purchase.getInvestorId()).getName());
            purchase.setGuarantorName(guarantorService.findGuarantor(purchase.getGuarantorId()).getName());
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return purchase;
    }

    @Override
    public Purchase showPurchaseByApplicationId(Integer applicationId) throws SQLException, IllegalArgumentException {
        Purchase purchase;
        LoanApplication application = applicationService.getApplicationById(applicationId);
        if(application == null){
            throw new IllegalArgumentException("未找到该借款申请");
        }
        if(!application.getStatus().equals(LoanStatus.SUBSCRIBED.getStatus())){
            throw new IllegalArgumentException("该借款申请未形成订单");
        }

        try{
            purchase = purchaseDao.getPurchaseByApplicationId(applicationId);
            List<RepayPlan> repayPlans = repayService.findPlanByPurchaseId(purchase.getPurchaseId());
            repayPlans.sort(Comparator.comparing(RepayPlan::getRepayDate));
            purchase.setRepayPlans(repayPlans);
            purchase.setBorrowerName(userService.findUser(purchase.getBorrowerId()).getName());
            purchase.setInvestorName(userService.findUser(purchase.getInvestorId()).getName());
            purchase.setGuarantorName(guarantorService.findGuarantor(purchase.getGuarantorId()).getName());
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
            purchaseList = setUserName(purchaseList);
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
            purchaseList = setUserName(purchaseList);
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
            purchaseList = setUserName(purchaseList);
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
            purchaseList = setUserName(purchaseList);
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
            purchaseList = setUserName(purchaseList);
        }
        catch (Exception e){
            throw new SQLException(e);
        }
        return new PageInfo<>(purchaseList);
    }

    private List<Purchase> setUserName(List<Purchase> list) throws Exception
    {
        for(Purchase purchase:list){
            purchase.setBorrowerName(userService.findUser(purchase.getBorrowerId()).getName());
            purchase.setInvestorName(userService.findUser(purchase.getInvestorId()).getName());
            purchase.setGuarantorName(guarantorService.findGuarantor(purchase.getGuarantorId()).getName());
        }
        return list;
    }

    @Transactional
    public void manualFinish()throws SQLException{
        log.info("检查订单完成状态");
        List<Purchase> purchaseList;
        try{
            purchaseList = purchaseDao.showAllPurchase();
        }
        catch (Exception e){
            throw new SQLException(e);
        }

        for(Purchase purchase:purchaseList){
            if(repayService.isRepayCompleted(purchase.getPurchaseId())){
                accomplishPurchase(purchase.getPurchaseId());
            }
        }
    }
}
