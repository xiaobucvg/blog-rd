package com.xiaobu.blog.controller;

import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.exception.AdminUserException;
import com.xiaobu.blog.exception.ArticleException;
import com.xiaobu.blog.exception.TokenGetException;
import com.xiaobu.blog.exception.ValidationException;
import com.xiaobu.blog.util.BindingResultUtil;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 统一异常处理
 *
 * @author zh  --2020/3/18 16:48
 */
@RestControllerAdvice
public class ExceptionAdviceController {

    /**
     * 字段验证异常处理器
     */
    @ExceptionHandler({ValidationException.class})
    public Response handleValidateException(ValidationException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> errorsMap = BindingResultUtil.getErrorsMap(result);
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "字段验证失败", errorsMap);
    }

    /**
     * 文章相关异常处理器
     */
    @ExceptionHandler({ArticleException.class})
    public Response handleValidateException(ArticleException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    /**
     * 权限异常处理器
     */
    @ExceptionHandler({AdminUserException.class})
    public Response handleAdminUserException(AdminUserException ex) {
        if (ex instanceof TokenGetException) {
            return Response.newFailInstance(HttpServletResponse.SC_FORBIDDEN, ex.getMessage());
        }
        return Response.newFailInstance(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }

    /**
     * 统一异常处理器
     */
    @ExceptionHandler({Exception.class})
    public Response handleException(Exception ex) {
        return Response.newFailInstance(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"发生内部错误");
    }
}
