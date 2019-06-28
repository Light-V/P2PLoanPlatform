package com.scut.p2ploanplatform.service.impl;

import com.scut.p2ploanplatform.config.InterestRateConfig;
import com.scut.p2ploanplatform.dao.InterestRateConfigDao;
import com.scut.p2ploanplatform.service.InterestRateConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Service
public class InterestRateConfigServiceImpl implements InterestRateConfigService {
    private final InterestRateConfigDao interestRateConfigDao;

    public InterestRateConfigServiceImpl(InterestRateConfigDao interestRateConfigDao) {
        this.interestRateConfigDao = interestRateConfigDao;
    }

    @Override
    public List<BigDecimal> getInterestRates() {
        return interestRateConfigDao.getInterestRates();
    }
}
