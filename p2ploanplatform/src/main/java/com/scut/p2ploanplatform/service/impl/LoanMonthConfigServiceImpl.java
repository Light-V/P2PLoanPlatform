package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.config.LoanMonthConfig;
import com.scut.p2ploanplatform.dao.LoanApplicationDao;
import com.scut.p2ploanplatform.dao.LoanMonthConfigDao;
import com.scut.p2ploanplatform.service.LoanMonthConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LoanMonthConfigServiceImpl implements LoanMonthConfigService {
    @Autowired
    LoanMonthConfigDao loanMonthConfigDao;
    @Override
    public List<LoanMonthConfig> getLoanmonths() {
        return loanMonthConfigDao.getLoanMonths();
    }
}
