package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.entity.Purchase;
import com.scut.p2ploanplatform.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PurchaseServiceImpl implements PurchaseService {
    @Autowired
    private PurchaseService purchaseService;

    @Override
    public Boolean createPurchaseItem(String investorId, Integer applicationId) {
        return null;
    }

    @Override
    public Boolean changePurchaseItemStatus(Integer purchaseId) {
        return null;
    }

    @Override
    public List<Purchase> showAllPurchase() {
        return null;
    }

    @Override
    public List<Purchase> showoPurchaseByInvestorId(String investorID) {
        return null;
    }

    @Override
    public Boolean accomplishPushchase(Integer purchaseId) {
        return null;
    }
}
