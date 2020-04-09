package com.xiaobu.blog.interceptor;

import com.xiaobu.blog.exception.AuthException;
import com.xiaobu.blog.util.NetUtil;
import com.xiaobu.blog.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private TokenUtil tokenUtil;

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
            throw new AuthException("没有权限访问，需要一个 auth-token");
        }
        if (!tokenUtil.isValidationToken(token)) {
            throw new AuthException("没有访问权限，使用了非法或已过期的 token");
        }
        // 每次访问后台接口，更新服务端地址信息，防止上传文件返回的 url 不正确
        NetUtil.resetServerAddress(request);
        return true;
    }
}
