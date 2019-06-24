package com.scut.p2ploanplatform.interceptor;

import com.scut.p2ploanplatform.enums.ResultEnum;
import com.scut.p2ploanplatform.exception.CustomException;
import com.scut.p2ploanplatform.utils.ResultVoUtil;
import com.scut.p2ploanplatform.vo.ResultVo;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * Created by zonghang
 * Date 2019/6/20 15:41
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResultVo httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest request,
                                                                  HttpRequestMethodNotSupportedException e) {
        return ResultVoUtil.error(405, String.format("%s %s, %s", e.getMethod(), request.getRequestURI(), e.getMessage()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultVo noHandlerFoundExceptionHandler(NoHandlerFoundException e) {
        return ResultVoUtil.error(404, e.getMessage());

    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo methodArgumentTypeMismatchExceptionHandler(MethodArgumentTypeMismatchException e) {
        return ResultVoUtil.error(ResultEnum.PARAM_TYPE_BIND_ERROR.getCode(),
                String.format("url参数错误：%s=%s", e.getName(), e.getValue()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder msg = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            msg.append(violation.getInvalidValue()).append(violation.getMessage()).append(",");
        }
        return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(), msg.substring(0, msg.lastIndexOf(",")));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVo bindExceptionHandler(BindException e) {
        StringBuilder msg = new StringBuilder();
        BindingResult bindingResult = e.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            msg.append(fieldError.getDefaultMessage()).append(", ");
        }
        return ResultVoUtil.error(ResultEnum.PARAM_IS_INVALID.getCode(), msg.substring(0, msg.lastIndexOf(",")));
    }

    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultVo customExceptionHandler(CustomException e) {
        return ResultVoUtil.error(e.getCode(), e.getMessage());
    }
}
