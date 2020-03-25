package com.xiaobu.blog.controller;

import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.exception.AdminUserException;
import com.xiaobu.blog.common.exception.ArticleException;
import com.xiaobu.blog.common.exception.TokenGetException;
import com.xiaobu.blog.common.exception.ValidationException;
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
public class AdviceController {

    /**
     * 处理字段验证异常
     */
    @ExceptionHandler({ValidationException.class})
    public Response handleValidateException(ValidationException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> errorsMap = BindingResultUtil.getErrorsMap(result);
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, "字段验证失败", errorsMap);
    }

    /**
     * 处理文章相关异常
     */
    @ExceptionHandler({ArticleException.class})
    public Response handleValidateException(ArticleException ex) {
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
    }

    /**
     * 处理权限异常
     */
    @ExceptionHandler({AdminUserException.class})
    public Response handlerAdminUserException(AdminUserException ex) {
        if(ex instanceof TokenGetException){
            return Response.newFailInstance(HttpServletResponse.SC_FORBIDDEN,ex.getMessage());
        }
        return Response.newFailInstance(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }
}
