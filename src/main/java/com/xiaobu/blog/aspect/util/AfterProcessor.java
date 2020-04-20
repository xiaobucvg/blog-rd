package com.xiaobu.blog.aspect.util;

/**
 * 对象后续处理器，配合 RequestJsonParamAspect 使用
 * RequestJsonParamAspect 将 json 成功转化为对象后可以使用后续处理器对对象进行后续处理
 *
 * @author zh  --2020/4/19 21:21
 */
public interface AfterProcessor<T> {
    Object process(T t);
}
