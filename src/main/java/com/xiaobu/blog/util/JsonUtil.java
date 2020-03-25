package com.xiaobu.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * Json 工具
 *
 * @author zh  --2020/3/25 14:49
 */
@Slf4j
public class JsonUtil {

    private JsonUtil(){

    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T stringToObject(String json,Class<T> clazz){
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("json{}转换为对象{}时发生错误",json,clazz);
        }
        return null;
    }

    public static String objectToJson(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("对象{}转换为json时发生错误",object);
        }
        return null;
    }

    public static byte[] objectToJsonBytes(Object object){
        try {
            return mapper.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("对象{}转换为json时发生错误",object);
        }
        return null;
    }
}
