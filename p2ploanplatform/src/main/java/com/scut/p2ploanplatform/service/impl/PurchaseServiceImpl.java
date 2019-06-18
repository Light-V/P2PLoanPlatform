package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author FatCat
 */
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Override
    public Boolean createPurchaseItem(String investorId, Integer applicationId) {
        return null;
    }

    @Override
    public Boolean purchaseOverdue(Integer purchaseId) {
        return null;
    }

    @Override
    public List<Purchase> showAllPurchase() {
        return null;
    }

    @Override
    public Purchase showPurchaseById(Integer purchaseId) {
        return null;
    }

    @Override
    public List<Purchase> showPurchaseByInvestorId(String investorID) {
        return null;
    }

    @Override
    public Boolean accomplishPurchase(Integer purchaseId) {
        return null;
    }

}
