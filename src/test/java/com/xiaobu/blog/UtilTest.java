package com.xiaobu.blog;

import com.xiaobu.blog.util.ExpiryMap;
import org.junit.jupiter.api.Test;

/**
 * @author zh  --2020/4/2 14:58
 */
public class UtilTest {
    ExpiryMap<String, String> map = new ExpiryMap<>();

    @Test
    void testPutExpiryMap() throws InterruptedException {
        map.put("1001", "value-1001", 3000);
        System.out.println(map.get("1001"));
        System.out.println(map.size());
        Thread.sleep(3000);
        System.out.println(map.get("1001"));
        System.out.println(map.size());
    }

    @Test
    void testGetExpiryMap() {
        System.out.println(map.get("1001"));
    }

    @Test
    void testGetEnv(){
        String property = System.getProperty("os.name");
        System.out.println(property);
    }

}
