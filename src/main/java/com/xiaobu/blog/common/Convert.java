package com.xiaobu.blog.common;

/**
 * 转换器，实现转换器的类可以将自身转换为实体类
 *
 * @author zh  --2020/3/17 20:58
 */
public interface Convert<T> {
    default T toModel(){
        throw new RuntimeException("没有实现该方法");
    }

    default Object toModel(T t){
        throw new RuntimeException("没有实现该方法");
    }
}
