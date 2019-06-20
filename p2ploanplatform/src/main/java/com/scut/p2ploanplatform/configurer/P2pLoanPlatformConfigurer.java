package com.scut.p2ploanplatform.configurer;

import com.scut.p2ploanplatform.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author: Light
 * @date: 2019/6/20 14:27
 * @description:
 */

@Configuration
public class P2pLoanPlatformConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new LoginInterceptor());
        interceptorRegistration.addPathPatterns("/**");
        //添加不拦截路径
        interceptorRegistration.excludePathPatterns("/user/login","/user/signup");

    }

}
