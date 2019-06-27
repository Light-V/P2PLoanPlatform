package com.scut.p2ploanplatform.utils;
import com.scut.p2ploanplatform.config.InterestRateConfig;
import com.scut.p2ploanplatform.config.LoanMonthConfig;
import com.scut.p2ploanplatform.service.InterestRateConfigService;
import com.scut.p2ploanplatform.service.LoanMonthConfigService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.util.List;
import java.util.logging.Logger;

@Data
@Service
@Slf4j
public class ConfigInitial implements CommandLineRunner {

    @Resource
    private LoanMonthConfigService loanMonthConfigService;
    @Resource
    private InterestRateConfigService interestRateConfigService;

    public static List<LoanMonthConfig> loanMonths;
    public static List<InterestRateConfig> interestRates;

    @Override
    public void run(String... args) throws Exception {
        loanMonths= loanMonthConfigService.getLoanmonths();
        interestRates= interestRateConfigService.getInterestRates();
        log.info("Auto load loanMonthConfig and InterestRateConfig");
//        System.out.println("The service has started.");
//        System.out.print(loanMonths);
//        System.out.print(interestRates);
    }
}
