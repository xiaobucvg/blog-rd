package com.xiaobu.blog.interceptor;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.exception.AdminUserException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 后台资源请求拦截器
 * <p>
 * 拦截所有需要管理员权限的请求
 *
 * @author zh  --2020/3/23 10:12
 */
@Component
public class AdminRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object user = request.getSession().getAttribute(Const.USER);
        if(user == null){
            throw new AdminUserException("没有权限!");
        }
        return true;
    }
}
