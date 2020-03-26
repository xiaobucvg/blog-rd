package com.xiaobu.blog.exception;

/**
 * 文章相关异常
 *
 * @author zh  --2020/3/18 14:31
 */
public class ArticleException extends RuntimeException {
    public ArticleException(String message) {
        super(message);
    }
}
