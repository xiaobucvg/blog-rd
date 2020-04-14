package com.xiaobu.blog.validator.annotation;

import com.xiaobu.blog.common.FileType;
import com.xiaobu.blog.validator.MultipartFileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zh  --2020/4/3 15:50
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = {MultipartFileValidator.class})
public @interface MultiPartFile {
    String message() default "{javax.validation.constraints.Image.message}";

    boolean required() default true;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    FileType type() default FileType.ALL;

    long maxSize() default Long.MAX_VALUE;

    long minSize() default 0;
}
