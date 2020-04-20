package com.xiaobu.blog.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaobu.blog.aspect.annotation.JsonParam;
import com.xiaobu.blog.aspect.util.AfterProcessor;
import com.xiaobu.blog.aspect.util.DefaultAfterProcessor;
import com.xiaobu.blog.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * 拦截所有方法参数中有 @JsonParam 的方法
 * 作用：
 * - 从拦截的方法的参数中获取第一个注解了 @JsonParam 的参数，获取该参数的值，需要确保值是一个 json 字符串
 * - 将字符串反序列化为对象，对象类型为 @JsonParam 的 value() 值
 * - 将反序列化后的对象赋值给紧跟参数的后面的对象，类型要与 value() 保持一致
 *
 * @author zh  --2020/3/19 21:36
 * @see JsonParam
 */
@Aspect
@Component
@Slf4j
public class RequestJsonParamAspect {

    @Autowired
    private JsonUtil jsonUtil;


    // 匹配参数中有 @GetTypeWithJson 注解的方法
    @Around("execution(* com.xiaobu.blog..*.*(@com.xiaobu.blog.aspect.annotation.JsonParam (*),..))")
    public Object around(ProceedingJoinPoint jp) throws Throwable {

        try {
            ArrayList<JsonWithValue> jsonWithValues = this.initBaseInfo(jp);

            this.jsonToValue(jsonWithValues, jp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("json 参数转换到对象发生错误");
            throw new RuntimeException("不能完成对象绑定");
        }
        return jp.proceed(jp.getArgs());
    }

    /**
     * 初始化基本信息
     *
     * @return list 初始化列表
     */
    private ArrayList<JsonWithValue> initBaseInfo(ProceedingJoinPoint jp) {
        ArrayList<JsonWithValue> list = new ArrayList<>();
        // 遍历参数，获取注解
        Object[] args = jp.getArgs();

        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        for (int i = parameters.length - 1; i >= 0; i--) {
            Parameter parameter = parameters[i];
            JsonParam annotation = parameter.getDeclaredAnnotation(JsonParam.class);

            if (annotation != null) {
                if (!(Objects.equals(parameter.getType(), String.class))) {
                    throw new RuntimeException(annotation.getClass().getName() + "只能注解 String 类型");
                }
                if (i + 1 >= parameters.length) {
                    throw new RuntimeException("初始化信息失败，参数后面需要一个对象接收转换后的值");
                }
                Class<?> clazz = annotation.value();
                if (Objects.equals(args[i + 1], clazz)) {
                    throw new RuntimeException("初始化信息失败，参数后面需要一个指定类型的的对象接收转换后的值");
                }
                list.add(new JsonWithValue(args[i].toString(), i + 1, annotation));
            }
        }
        return list;
    }

    /**
     * 转换
     */
    private void jsonToValue(ArrayList<JsonWithValue> list, ProceedingJoinPoint jp) throws JsonProcessingException {
        for (JsonWithValue jsonWithValue : list) {
            Object res = jsonUtil.stringToObject(jsonWithValue.json, jsonWithValue.jsonParam.value());
            // 后续处理器处理
            this.afterProcess(res, jsonWithValue.jsonParam);
            // jsr 验证字段
            this.validate(res, jsonWithValue.jsonParam);
            jp.getArgs()[jsonWithValue.valueIndex] = res;
        }
    }

    /**
     * 后续处理器处理
     *
     * @param obj                      要处理的对象
     * @param jsonParam 注解
     */
    private void afterProcess(Object obj, JsonParam jsonParam) {
        Class<? extends AfterProcessor>[] classes = jsonParam.afterProcessor();
        if (classes.length == 1 && Objects.equals(classes[0], DefaultAfterProcessor.class)) {
            return;
        }
        for (Class<? extends AfterProcessor> processor : classes) {
            if (!Objects.equals(processor, DefaultAfterProcessor.class)) {
                try {
                    processor.getConstructor().newInstance().process(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("{}-后续处理发生错误", obj.getClass().getName());
                }
            }
        }
    }

    /**
     * 字段验证
     *
     * @param obj                      要验证的对象
     * @param jsonParam 注解
     */
    private void validate(Object obj, JsonParam jsonParam) {
        if (!jsonParam.needValidate()) {
            return;
        }
        Set<ConstraintViolation<Object>> res = Validation.buildDefaultValidatorFactory().getValidator().validate(obj);
        if (res.size() > 0) {
            throw new ConstraintViolationException(res);
        }
    }


    private static class JsonWithValue {
        JsonParam jsonParam;
        String json;   // json
        int valueIndex;// 接收转换值的参数索引位置

        JsonWithValue(String json, int valueIndex, JsonParam jsonParam) {
            this.json = json;
            this.valueIndex = valueIndex;
            this.jsonParam = jsonParam;
        }
    }

}
