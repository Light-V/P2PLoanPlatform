package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.dao.P2pAccountDao;
import com.scut.p2ploanplatform.service.P2pAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class P2pAccountServiceImpl implements P2pAccountService {
    @Autowired
    P2pAccountDao p2pAccountDao;
    @Override
    public BigDecimal showBalance(String userId)
    {

    }
}
