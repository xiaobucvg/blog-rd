package com.xiaobu.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaobu.blog.util.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zh  --2020/4/20 16:39
 */
@SpringBootTest
public class JsonTest {

    @Autowired
    private JsonUtil jsonUtil;

    @Test
    void json() throws JsonProcessingException {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "zhangsan");
        map.put("age", "12");
        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("xx-1", "hulu");
        map2.put("xx-2", "diluma");
        map.put("xx", map2);
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        map.put("ll",list);
        String res = jsonUtil.objectToJson(map);
        System.out.println(res);
    }

    @Test
    void string() throws JsonProcessingException {
        HashMap map = jsonUtil.stringToObject("{\"xx\":{\"xx-1\":\"hulu\",\"xx-2\":\"diluma\"},\"ll\":[\"1\",\"2\",\"3\"],\"name\":\"zhangsan\",\"age\":\"12\"}", HashMap.class);
        System.out.println(map);
    }
}
