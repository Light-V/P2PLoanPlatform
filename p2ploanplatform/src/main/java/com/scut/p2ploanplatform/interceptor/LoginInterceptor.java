package com.scut.p2ploanplatform.interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author: Light
 * @date: 2019/6/20 14:01
 * @description:
 */


public class LoginInterceptor implements HandlerInterceptor {

    //前置拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        System.out.println("拦截:"+request.getRequestURI());
        if (session.getAttribute("user") == null) {
                System.out.println("无效session");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return false;
        }
        //否则正常操作
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
