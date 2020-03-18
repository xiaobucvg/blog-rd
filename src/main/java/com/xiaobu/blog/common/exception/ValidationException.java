package com.xiaobu.blog.common.exception;

import lombok.Data;
import org.springframework.validation.BindingResult;

/**
 * 字段验证异常
 *
 * @author zh  --2020/3/18 16:46
 */
@Data
public class ValidationException extends RuntimeException {

    private BindingResult bindingResult;

    public ValidationException(BindingResult bindingResult,String msg) {
        super(msg);
        this.bindingResult = bindingResult;
    }
}
