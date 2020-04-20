package com.xiaobu.blog.aspect.annotation;

import com.xiaobu.blog.aspect.util.AfterProcessor;
import com.xiaobu.blog.aspect.util.DefaultAfterProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解到方法参数中，指定该参数是一个 json 字符串，后面紧跟 json 字符串转化后的对象参数
 * 配合 RequestJsonParamAspect 使用
 *
 * @author zh  --2020/3/19 21:39
 * @see com.xiaobu.blog.aspect.RequestJsonParamAspect
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonParam {
    // 要反序列化的对象类型,必须提供
    Class<?> value();

    // 是否需要校验参数合法性
    boolean needValidate() default true;

    // 指定后续处理器
    Class<? extends AfterProcessor>[] afterProcessor() default DefaultAfterProcessor.class;
}
