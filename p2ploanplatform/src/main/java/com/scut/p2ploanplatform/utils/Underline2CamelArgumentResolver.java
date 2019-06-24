package com.scut.p2ploanplatform.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.validation.Valid;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zonghang
 * Date 2019/6/23 22:56
 */
@Slf4j
public class Underline2CamelArgumentResolver implements HandlerMethodArgumentResolver {

    private static Pattern pattern = Pattern.compile("_(\\w)");

    private static String underLineToCamel(String source) {
        Matcher matcher = pattern.matcher(source);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(ParamModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object resolvedObject =  handleParameterNames(methodParameter, nativeWebRequest);
        if (methodParameter.hasParameterAnnotation(Valid.class) || methodParameter.hasParameterAnnotation(Validated.class)) {
            WebDataBinder binder = webDataBinderFactory.createBinder(nativeWebRequest, resolvedObject, Conventions.getVariableNameForParameter(methodParameter));
            binder.validate();
            BindingResult bindingResult = binder.getBindingResult();
            if (bindingResult.hasErrors()) {
                throw new BindException(bindingResult);
            }
        }
        return resolvedObject;
    }

    private Object handleParameterNames(MethodParameter parameter, NativeWebRequest webRequest) {
        Object obj = BeanUtils.instantiateClass(parameter.getParameterType());
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        Iterator<String> parameterNames = webRequest.getParameterNames();
        while (parameterNames.hasNext()) {
            String parameterName = parameterNames.next();
            Object o = webRequest.getParameter(parameterName);
            try {
                wrapper.setPropertyValue(underLineToCamel(parameterName), o);
            } catch (BeansException e) {
                log.error("[Underline2CamelArgumentResolver] error: {}", e.getMessage());
            }
        }
        return obj;
    }
}
