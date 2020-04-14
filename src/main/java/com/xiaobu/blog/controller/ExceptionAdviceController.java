package com.xiaobu.blog.controller;

import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.exception.*;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * 统一异常处理器
 *
 * @author zh  --2020/3/18 16:48
 */
@RestControllerAdvice
public class ExceptionAdviceController {

    private static final String PRE_MESSAGE = "";

    @ExceptionHandler({ConstraintViolationException.class,
            MethodArgumentNotValidException.class,
            ServletRequestBindingException.class,
            BindException.class})
    public Response handleValidationException(Exception e) {
        String msg = "";
        if (e instanceof MethodArgumentNotValidException) {
            msg = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        } else if (e instanceof BindException) {
            msg = ((BindException) e).getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        } else if (e instanceof ConstraintViolationException) {
            ConstraintViolationException t = (ConstraintViolationException) e;
            msg = t.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(","));
        } else if (e instanceof MissingServletRequestParameterException) {
            MissingServletRequestParameterException t = (MissingServletRequestParameterException) e;
            msg = t.getParameterName() + " 不能为空";
        } else if (e instanceof MissingPathVariableException) {
            MissingPathVariableException t = (MissingPathVariableException) e;
            msg = t.getVariableName() + " 不能为空";
        } else {
            msg = "必填参数缺失";
        }
        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, msg);
    }

//    /**
//     * bean 邦定值校验失败
//     * 异常异常处理器
//     */
//    @ExceptionHandler({MethodArgumentNotValidException.class})
//    public Response handleValidateException(MethodArgumentNotValidException ex) {
//        BindingResult result = ex.getBindingResult();
//        Map<String, String> errorsMap = new LinkedHashMap<>();
//        result.getFieldErrors().forEach(error -> {
//            errorsMap.put(error.getField(), error.getDefaultMessage());
//        });
//        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, PRE_MESSAGE + "参数校验失败", errorsMap);
//    }
//
//    /**
//     * 方法参数校验失败
//     * 异常处理器
//     */
//    @ExceptionHandler({ConstraintViolationException.class})
//    public Response handleValidateException(ConstraintViolationException ex) {
//        Map<String, String> errorsMap = new LinkedHashMap<>();
//        ex.getConstraintViolations().forEach(val -> {
//            errorsMap.put(val.getPropertyPath().toString(), val.getMessage());
//        });
//        return Response.newFailInstance(HttpServletResponse.SC_BAD_REQUEST, PRE_MESSAGE + "参数校验失败", errorsMap);
//    }

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
