package com.xiaobu.blog.exception;

/**
 * 文件上传异常
 * @author zh  --2020/4/9 20:42
 */
public class FileUploadException extends RuntimeException{
    public FileUploadException() {
        super();
    }

    public FileUploadException(String message) {
        super(message);
    }
}
