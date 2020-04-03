package com.xiaobu.blog.controller;

import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.exception.ArticleException;
import com.xiaobu.blog.exception.TokenException;
import com.xiaobu.blog.exception.UserException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一异常处理
 *
 * @author zh  --2020/3/18 16:48
 */
@RestControllerAdvice
public class ExceptionAdviceController {

    /**
     * bean 邦定值校验失败
     * 异常异常处理器
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public Response handleValidateException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> errorsMap = new LinkedHashMap<>();
        result.getFieldErrors().forEach(error -> {
            errorsMap.put(error.getField(), error.getDefaultMessage());
        });
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "字段验证失败", errorsMap);
    }

    /**
     * 方法参数校验失败
     * 异常处理器
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public Response handleValidateException(ConstraintViolationException ex) {
        Map<String, String> errorsMap = new LinkedHashMap<>();
        ex.getConstraintViolations().forEach(val -> {
            errorsMap.put(val.getPropertyPath().toString(), val.getMessage());
        });
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "字段验证失败", errorsMap);
    }

    /**
     * 文章相关
     * 异常处理器
     */
    @ExceptionHandler({ArticleException.class})
    public Response handleValidateException(ArticleException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    /**
     * 权限相关
     * 异常处理器
     */
    @ExceptionHandler({UserException.class})
    public Response handleUserException(UserException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Token 相关
     * 异常处理器
     */
    @ExceptionHandler({TokenException.class})
    public Response handleTokenException(UserException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
    }

    /**
     * 统一
     * 异常处理器
     */
    @ExceptionHandler({Exception.class})
    public Response handleException(Exception ex) {
        return Response.newFailInstance(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "发生内部错误");
    }
}
