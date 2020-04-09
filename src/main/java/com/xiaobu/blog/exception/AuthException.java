package com.xiaobu.blog.exception;

/**
 * 权限异常
 *
 * @author zh  --2020/4/9 21:08
 */
public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
