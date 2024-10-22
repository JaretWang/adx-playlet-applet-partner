package com.dataeye.partner.common.exception;

import com.dataeye.partner.bean.response.ApiResponse;
import com.dataeye.partner.bean.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.UnknownSessionException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author jaret
 * @date 2024/10/21 17:57
 * @description
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ApiResponse<String> topException(Throwable e) {
        e.printStackTrace();
        return ApiResponse.error(StatusCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(UnknownSessionException.class)
    public ApiResponse<String> handleUnknownSessionException(Throwable e) {
        log.error("未知会话异常: {}", e.getMessage(), e);
        return ApiResponse.error(StatusCode.INTERNAL_SERVER_ERROR, "未知会话异常");
    }

    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class, BindException.class})
    @ResponseBody
    public ApiResponse<String> handleJsr349ValidException(Throwable e) {
        log.info("参数校验异常: {}", e.getMessage(), e);
        String msg = e.getMessage();
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException ee = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> message = ee.getConstraintViolations();
            List<String> errorMessageList = new ArrayList<>();
            message.forEach(msg1 -> {
                errorMessageList.add(msg1.getMessageTemplate());
            });
            msg = errorMessageList.toString();
        }

        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ee = (MethodArgumentNotValidException) e;
            // 获取BindingResult对象，包含验证错误信息
            BindingResult bindingResult = ee.getBindingResult();
            msg = parseErrorMsg(bindingResult);
        }

        if (e instanceof BindException) {
            BindException ee = (BindException) e;
            BindingResult bindingResult = ee.getBindingResult();
            msg = parseErrorMsg(bindingResult);
        }
        return ApiResponse.error(StatusCode.INTERNAL_SERVER_ERROR, msg);
    }


    String parseErrorMsg(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(fieldError -> {
            String splitStr = fieldErrors.size() == 1 ? "" : ";";
            errorMessage.append(fieldError.getDefaultMessage()).append(splitStr);
        });
        return errorMessage.toString();
    }

    // 捕获 Shiro 未登录异常
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<String> handleShiroAuthenticationException(AuthenticationException e) {
        return ApiResponse.error(StatusCode.UNAUTHORIZED, "未登录，无法访问此资源");
    }

    // 捕获 Shiro 未授权异常
    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<String> handleShiroAuthorizationException(AuthorizationException e) {
        return ApiResponse.error(StatusCode.FORBIDDEN, "无权访问此资源");
    }
}
