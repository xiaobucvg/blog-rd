package com.xiaobu.blog.exception;

/**
 * 获取 Token 异常
 *
 * @author zh  --2020/3/25 14:24
 */
public class TokenException extends RuntimeException{
    public TokenException(String message) {
        super(message);
    }
}
