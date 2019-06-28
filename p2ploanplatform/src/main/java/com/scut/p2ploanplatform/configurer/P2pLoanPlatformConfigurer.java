package com.scut.p2ploanplatform.configurer;

import com.scut.p2ploanplatform.interceptor.LoginInterceptor;
import com.scut.p2ploanplatform.utils.Underline2CamelArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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
        interceptorRegistration.excludePathPatterns(
                "/user/login",
                "/user/signup",
                "/error",
                "/loan_application/detail/*",   //产品详情页
                "/loan_application/onsale",
                "/third_party/*"
        );

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new Underline2CamelArgumentResolver());
    }
}
