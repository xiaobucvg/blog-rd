package com.xiaobu.blog.validator;

import com.xiaobu.blog.common.FileType;
import com.xiaobu.blog.validator.annotation.MultiPartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

/**
 * 验证文件
 *
 * @author zh  --2020/4/3 15:36
 */
@Component
public class MultipartFileValidator implements ConstraintValidator<MultiPartFile, MultipartFile> {

    private boolean required;
    private long maxSize;
    private long minSize;
    private FileType type;

    @Override
    public void initialize(MultiPartFile constraintAnnotation) {
        this.required = constraintAnnotation.required();
        this.maxSize = constraintAnnotation.maxSize();
        this.minSize = constraintAnnotation.minSize();
        this.type = constraintAnnotation.type();
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        // 必须要有值
        if (this.required && (value == null || value.isEmpty())) {
            return false;
        }
        if (value != null) {
            long size = value.getSize(); // byte

            // 超过最大大小
            if (size > this.maxSize) {
                return false;
            }

            // 小于最小大小
            if (size < this.minSize) {
                return false;
            }

            // 类型匹配
            if (this.type.getMime().length > 0) {
                String mime = Objects.requireNonNull(value.getContentType()).toLowerCase();
                for (String t : this.type.getMime()) {
                    if (Objects.equals(t.toLowerCase(), mime)) {
                        return true;
                    }
                }
                return false;
            }
        }
        return true;
    }
}
