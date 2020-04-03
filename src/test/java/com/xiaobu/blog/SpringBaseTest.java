package com.xiaobu.blog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaobu.blog.common.token.Token;
import com.xiaobu.blog.util.FileUploadUtil;
import com.xiaobu.blog.util.TokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.WebDataBinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author zh  --2020/4/2 15:36
 */
@SpringBootTest
public class SpringBaseTest {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private FileUploadUtil fileUploadUtil;

    String tokenStr = "eyJ0eXAiOiJqd3QiLCJhbGciOiJIbWFjU0hBMjU2In0=.eyJzdWIiOiIxMDAxIiwiaXNzIjoiaHR0cDovLzE5Mi4xNjguNTYuMTo4MDgwL2FkbWluL3Rva2VuIiwiaWF0IjoiMTU4NTgxMzcxNTIzMSIsIm5iZiI6IjE1ODU4MTM3MTUyMzEiLCJleHAiOiIxNTg1ODE3MzE1MjMxIiwianRpIjoiMjgxNjVjMGMtODI4Ni00MDYzLWI0ZGMtNDQ3ZThkYjZiMDNhIn0=.884D80D13D72C0D086709EBAA98CC1723D0B2EC50262B22A628432365F6A9BD2";


    @Test
    void testGetToken() throws Exception {
        String token = tokenUtil.getToken("1001");
        System.out.println(token);
    }

    @Test
    void testParseToken() throws JsonProcessingException {
        Token token = tokenUtil.parseToken(tokenStr);
        System.out.println(token);
    }

    @Test
    void testValidateToken() {
        System.out.println(tokenUtil.isValidationToken(tokenStr));
    }

    @Test
    void testUploadFile() throws IOException {
        File file = new File("README.md");
        FileInputStream fIn = new FileInputStream(file);
        byte[] bytes = new byte[fIn.available()];
        fIn.read(bytes);
        fileUploadUtil.uploadFile(bytes,"文件名字.md");
    }
}
