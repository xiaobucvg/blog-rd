package com.xiaobu.blog.config;

import com.xiaobu.blog.interceptor.AdminRequestInterceptor;
import com.xiaobu.blog.interceptor.CrossOriginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zh  --2020/3/23 15:21
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminRequestInterceptor adminRequestInterceptor;

    @Autowired
    private CrossOriginInterceptor crossOriginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(crossOriginInterceptor).addPathPatterns(crossOriginInterceptor.getPathPatterns());
        registry.addInterceptor(adminRequestInterceptor)
                .addPathPatterns(adminRequestInterceptor.getPathPatterns())
                .excludePathPatterns(adminRequestInterceptor.getExcludePathPatterns());
    }
}