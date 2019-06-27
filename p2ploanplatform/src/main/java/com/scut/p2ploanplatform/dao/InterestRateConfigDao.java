package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.config.InterestRateConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
@Repository
@Mapper
public interface InterestRateConfigDao {
    @Select("SELECT * FROM `loan_interest_rate`")
    List<InterestRateConfig> getInterestRates();
}
