package com.xiaobu.blog.validator;

import com.xiaobu.blog.validator.annotation.MultiPartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

/**
 * 验证文件
 *
 * @author zh  --2020/4/3 15:36
 */
@Component
@Slf4j
public class MultipartFileValidator implements ConstraintValidator<MultiPartFile, MultipartFile> {

    private MultiPartFile constraintAnnotation;

    @Override
    public void initialize(MultiPartFile constraintAnnotation) {
        this.constraintAnnotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value != null)
            return this.validate(value, context);
        return true;
    }

    private boolean validate(MultipartFile value, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        // 必须要有值
        if (constraintAnnotation.required() && (value == null || value.isEmpty())) {
            context.buildConstraintViolationWithTemplate("您没有上传任何东西,必须上传一个文件").addPropertyNode(":").addConstraintViolation();
            return false;
        }
        // 超过最大大小
        long size = value.getSize(); // byte
        if (size > constraintAnnotation.maxSize()) {
            context.buildConstraintViolationWithTemplate("文件大小大于最大值" + constraintAnnotation.maxSize()).addPropertyNode(":").addConstraintViolation();
            return false;
        }
        // 小于最小大小
        if (size < constraintAnnotation.minSize()) {
            context.buildConstraintViolationWithTemplate("文件大小小于最小值" + constraintAnnotation.maxSize()).addPropertyNode(":").addConstraintViolation();
            return false;
        }
        // 类型匹配
        if (constraintAnnotation.type().length > 0 && !constraintAnnotation.type()[0].equals(MimeTypeUtils.ALL_VALUE)) {
            String mime = Objects.requireNonNull(value.getContentType()).toLowerCase();
            for (String t : constraintAnnotation.type()) {
                if (Objects.equals(t.toLowerCase(), mime) || Objects.equals(t, MimeTypeUtils.ALL_VALUE)) {
                    return true;
                }
            }
            context.buildConstraintViolationWithTemplate("不支持此文件类型，支持的上传类型为" + Arrays.toString(constraintAnnotation.type())).addPropertyNode(":").addConstraintViolation();
            return false;
        }
        return true;
    }
}
