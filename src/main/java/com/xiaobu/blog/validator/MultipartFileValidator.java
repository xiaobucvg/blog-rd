package com.xiaobu.blog.validator;

import com.xiaobu.blog.validator.annotation.MultiPartFile;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * @author zh  --2020/4/3 15:36
 */
@Component
public class MultipartFileValidator implements ConstraintValidator<MultiPartFile, MultipartFile> {

    private boolean canEmpty;
    private long maxSize;
    private long minSize;
    private String type;

    @Override
    public void initialize(MultiPartFile constraintAnnotation) {
        this.canEmpty = constraintAnnotation.canEmpty();
        this.maxSize = constraintAnnotation.maxSize();
        this.minSize = constraintAnnotation.minSize();
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        long size = value.getSize(); // byte
        String type = value.getContentType();
        // 不可以为空
        if (!this.canEmpty && size == 0) {
            return false;
        }
        // 超过最大大小
        if (size > this.maxSize) {
            return false;
        }
        // 小于最小大小
        if (size < this.minSize) {
            return false;
        }
        // 类型不匹配
        if (!StringUtils.isEmpty(this.type) && Objects.equals(type.toLowerCase(), this.type.toLowerCase())) {
            return false;
        }
        return true;
    }
}
