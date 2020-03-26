package com.xiaobu.blog.aspect;

import com.xiaobu.blog.common.page.Pageable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * 拦截所有注解了
 *
 * @author zh  --2020/3/26 18:44
 * @see com.xiaobu.blog.aspect.annotation.PageableAutoCalculate
 * 的方法
 * 自动计算参数中分页参数的起始位置和结束位置
 */
@Aspect
@Component
@Order(101)
public class PageableAutoCalculateAspect {

    @Before("@annotation(com.xiaobu.blog.aspect.annotation.PageableAutoCalculate)")
    public void autoCalculatePage(JoinPoint joinPoint) {
        Arrays.stream(joinPoint.getArgs()).forEach(arg -> {
            if(arg instanceof Pageable){
                ((Pageable) arg).calculate();
            }
        });
    }

}
