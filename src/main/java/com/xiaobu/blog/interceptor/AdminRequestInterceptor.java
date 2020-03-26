package com.xiaobu.blog.interceptor;

import com.xiaobu.blog.exception.AdminUserException;
import com.xiaobu.blog.exception.ExpiresTokenException;
import com.xiaobu.blog.exception.IllegalTokenException;
import com.xiaobu.blog.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 后台资源请求拦截器
 * 拦截所有需要管理员权限的请求
 *
 * @author zh  --2020/3/23 10:12
 */
@Component
public class AdminRequestInterceptor implements HandlerInterceptor {

    private List<String> pathPatterns = new ArrayList<>();

    private List<String> excludePathPatterns = new ArrayList<>();

    public AdminRequestInterceptor() {
        this.pathPatterns.add("/admin/**");
        excludePathPatterns.add("/admin/token");
    }

    public List<String> getPathPatterns() {
        return pathPatterns;
    }

    public List<String> getExcludePathPatterns() {
        return excludePathPatterns;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("auth-token");
        if (token == null) {
            throw new AdminUserException("没有权限");
        }
        try {
            TokenUtil.checkToken(token);
        } catch (IllegalTokenException e) {
            e.printStackTrace();
            throw new AdminUserException("token非法");
        } catch (ExpiresTokenException e) {
            e.printStackTrace();
            throw new AdminUserException("token已过期");
        }
        return true;
    }
}