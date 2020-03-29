package com.xiaobu.blog.aspect;

import com.xiaobu.blog.exception.LogException;
import com.xiaobu.blog.model.Log;
import com.xiaobu.blog.service.LogService;
import com.xiaobu.blog.util.InetUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 日志记录
 *
 * @author zh  --2020/3/29 11:47
 */
@Aspect
@Component
public class LogAspect {

    @Autowired
    private LogService logService;

    @AfterReturning(value = "@annotation(com.xiaobu.blog.aspect.annotation.Log)")
    public void log(JoinPoint joinPoint) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = InetUtil.getIpAddress(request);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        com.xiaobu.blog.aspect.annotation.Log an = methodSignature.getMethod().getAnnotation(com.xiaobu.blog.aspect.annotation.Log.class);
        String logMsg = "";
        try {
            logMsg = (String) an.getClass().getDeclaredMethod("value").invoke(an);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("无法获取 log 内容");
        }

        Log log = new Log();
        log.setIp(ip);
        log.setMsg(logMsg);
        log.setCreateTime(new Date());
        log.setUpdateTime(new Date());

        this.saveLog(log);

    }

    @Async
    public void saveLog(Log log) {
        try {
            logService.saveLog(log);
        } catch (LogException e) {
            e.printStackTrace();
        }
    }
}
