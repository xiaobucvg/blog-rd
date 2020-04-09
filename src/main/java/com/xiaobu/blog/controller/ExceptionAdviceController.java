package com.xiaobu.blog.controller;

import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.exception.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 统一异常处理器
 *
 * @author zh  --2020/3/18 16:48
 */
@RestControllerAdvice
public class ExceptionAdviceController {

    private static final String PRE_MESSAGE = "异常：";

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
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, PRE_MESSAGE + "字段验证失败", errorsMap);
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
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, PRE_MESSAGE + "字段验证失败", errorsMap);
    }

    /**
     * 文章相关
     * 异常处理器
     */
    @ExceptionHandler({ArticleException.class})
    public Response handleValidateException(ArticleException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, PRE_MESSAGE + ex.getMessage());
    }

    /**
     * 文件上传
     * 异常处理器
     */
    @ExceptionHandler({FileUploadException.class})
    public Response handleFileUploadException(FileUploadException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, PRE_MESSAGE + ex.getMessage());
    }


    /**
     * 用户操作
     * 异常处理器
     */
    @ExceptionHandler({UserException.class})
    public Response handleUserException(UserException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, PRE_MESSAGE + ex.getMessage());
    }

    /**
     * 权限相关
     * 异常处理器
     */
    @ExceptionHandler({AuthException.class})
    public Response handleAuthException(AuthException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_UNAUTHORIZED, PRE_MESSAGE + ex.getMessage());
    }

    /**
     * Token 相关
     * 异常处理器
     */
    @ExceptionHandler({TokenException.class})
    public Response handleTokenException(TokenException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_FORBIDDEN, PRE_MESSAGE + ex.getMessage());
    }

    /**
     * 统一
     * 异常处理器
     */
    @ExceptionHandler({Exception.class})
    public Response handleException(Exception ex) {
        return Response.newFailInstance(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, PRE_MESSAGE + "内部错误");
    }
}
