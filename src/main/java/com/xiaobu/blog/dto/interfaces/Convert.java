package com.xiaobu.blog.dto.interfaces;

/**
 * 转换器，实现转换器的类可以将自身转换为实体类，或者复制到自身
 *
 * @author zh  --2020/3/17 20:58
 */
public interface Convert<T> {
    default T copyFrom() {
        throw new RuntimeException("没有实现该方法");
    }

    default Object copyFrom(T t) {
        throw new RuntimeException("没有实现该方法");
    }
}
