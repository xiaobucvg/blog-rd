package com.xiaobu.blog.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaobu.blog.util.JsonUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 拦截所有注解了 @RequestJsonParamToObject 的方法
 * <p>
 * 作用：
 * - 从拦截的方法的参数中获取第一个注解了 @RequestParam 的参数，获取该参数的值，需要确保值是一个可已被反序列化的字符串.
 * - 将字符串反序列化为对象，对象类型为 @RequestJsonParamToObject 的 value() 值.
 * - 将反序列化后的对象赋值给第一个注解了 @RequestParam 的参数的后一个对象，需要确保该对象的类型与@RequestJsonParamToObject 的 value() 值相同.
 *
 * @author zh  --2020/3/19 21:36
 * @see RequestJsonParamToObject
 */
@Aspect
@Component
public class RequestJsonParamAspect {

    @Autowired
    Validator globalValidator;

    // 匹配参数中有 @GetTypeWithJson 注解的方法
    @Pointcut("@annotation(com.xiaobu.blog.aspect.RequestJsonParamToObject)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint jp) throws Throwable {

        // 1. 获取注解了 @GetTypeWithJson 的参数位置
        //    获取注解的值
        int index = -1;
        Class clazz = null;

        MethodSignature signature = (MethodSignature) jp.getSignature();
        Method method = signature.getMethod();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType().equals(RequestParam.class)) {
                    index = i;
                    i = parameterAnnotations.length;
                }
            }
        }
        RequestJsonParamToObject an = signature.getMethod().getAnnotation(RequestJsonParamToObject.class);
        clazz = (Class) an.getClass().getDeclaredMethod("value").invoke(an);

        // 2. 确保没有异常
        if (index == -1 || clazz == null) {
            throw new RuntimeException("@GetTypeWithJson 使用错误,@RequestParam 不存在.");
        }

        // 3. 将此参数转换为对象,并赋值给后一个对象
        Object[] args = jp.getArgs();
        String json = (String) args[index];

        if (!Objects.equals(args[index + 1].getClass(), clazz)) {
            throw new RuntimeException("@GetTypeWithJson 使用错误,参数类型不匹配");
        }

        Object obj = null;
        obj = JsonUtil.stringToObject(json, clazz);
        if(obj == null){
            throw new RuntimeException("@GetTypeWithJson 使用错误,无法将 Json 反序列化.");
        }
        args[index + 1] = obj;

        // 4. 检查是否需要校验参数
        if (args.length > index + 2 && Objects.equals(args[index + 2].getClass(), BeanPropertyBindingResult.class)) {
            DataBinder dataBinder = new DataBinder(obj);
            dataBinder.setValidator((org.springframework.validation.Validator) globalValidator);
            dataBinder.validate();
            BindingResult bindingResult = dataBinder.getBindingResult();
            args[index + 2] = bindingResult;
        }

        return jp.proceed(args);
    }


}
