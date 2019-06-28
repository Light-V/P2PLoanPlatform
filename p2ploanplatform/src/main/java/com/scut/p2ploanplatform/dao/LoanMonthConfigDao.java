package com.scut.p2ploanplatform.dao;

import com.scut.p2ploanplatform.config.LoanMonthConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface LoanMonthConfigDao {

    @Select("SELECT * FROM `loan_month`")
    List<Integer> getLoanMonths();
}
