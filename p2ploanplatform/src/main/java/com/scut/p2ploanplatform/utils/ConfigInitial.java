package com.scut.p2ploanplatform.utils;
import com.scut.p2ploanplatform.service.InterestRateConfigService;
import com.scut.p2ploanplatform.service.LoanMonthConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;


@Service
@Slf4j
public class ConfigInitial implements CommandLineRunner {

    @Resource
    private LoanMonthConfigService loanMonthConfigService;
    @Resource
    private InterestRateConfigService interestRateConfigService;

    public static List<Integer> loanMonths;
    public static List<BigDecimal> interestRates;

    @Override
    public void run(String... args){
        loanMonths= loanMonthConfigService.getLoanmonths();
        interestRates= interestRateConfigService.getInterestRates();
        log.info("Auto load loanMonthConfig and InterestRateConfig");
//        System.out.println(loanMonths);
//        System.out.println(interestRates);
    }
}
