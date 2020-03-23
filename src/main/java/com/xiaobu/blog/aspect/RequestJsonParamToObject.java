package com.xiaobu.blog.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解到方法上,指示该方法是一个接收 json 字符串的 GET 方法.
 *
 * 配合 RequestJsonParamAspect 使用.
 * @see com.xiaobu.blog.aspect.RequestJsonParamAspect
 * @author zh  --2020/3/19 21:39
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestJsonParamToObject {
    // 要反序列化的对象类型,必须提供
    Class<?> value();
}
