package com.scut.p2ploanplatform.service;

import com.scut.p2ploanplatform.config.InterestRateConfig;

import java.math.BigDecimal;
import java.util.List;

public interface InterestRateConfigService {
    List<BigDecimal> getInterestRates();
}
