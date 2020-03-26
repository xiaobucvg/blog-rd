package com.xiaobu.blog.interceptor;

import com.xiaobu.blog.common.Const;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 跨域控制拦截器
 *
 * @author zh  --2020/3/26 9:17
 */
@Component
public class CrossOriginInterceptor implements HandlerInterceptor {

    private List<String> pathPatterns = new ArrayList<>();

    public CrossOriginInterceptor() {
        this.pathPatterns.add("/**");
    }

    public List<String> getPathPatterns() {
        return pathPatterns;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST,PUT, GET, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", Const.TOKEN + ", Content-Type");
        response.setHeader("Access-Control-Max-Age", " 86400");

        if (request.getMethod().toUpperCase().equals("OPTIONS")) {
            response.setStatus(HttpStatus.NO_CONTENT.value());
        }
        return true;
    }
}
