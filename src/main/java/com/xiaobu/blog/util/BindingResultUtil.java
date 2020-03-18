package com.xiaobu.blog.util;

import org.springframework.validation.BindingResult;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zh  --2020/3/18 17:04
 */
public class BindingResultUtil {
    public static Map<String,String> getErrorsMap(BindingResult bindingResult){
        Map<String ,String> res = new LinkedHashMap<>();
        bindingResult.getFieldErrors().forEach(error -> {
            res.put(error.getField(),error.getDefaultMessage());
        });
        return res;
    }
}
