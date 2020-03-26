package com.xiaobu.blog.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分页自动计算
 * 表明被注解的参数需要自动计算分页查询对象的起始位置和结束位置
 * @see com.xiaobu.blog.aspect.PageableAutoCalculateAspect
 * @author zh  --2020/3/26 18:42
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageableAutoCalculate {

}
