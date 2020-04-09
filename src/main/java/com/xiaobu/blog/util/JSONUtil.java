package com.xiaobu.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Json 工具，序列化与反序列化
 *
 * @author zh  --2020/3/25 14:49
 */
@Slf4j
@Component
public class JSONUtil {

    private ObjectMapper mapper = new ObjectMapper();

    public <T> T stringToObject(String json, Class<T> clazz) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }

    public String objectToJson(Object object) throws JsonProcessingException {
        return mapper.writeValueAsString(object);
    }

    public byte[] objectToJsonBytes(Object object) throws JsonProcessingException {
        return mapper.writeValueAsBytes(object);
    }
}
