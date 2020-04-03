package com.xiaobu.blog;

import com.xiaobu.blog.util.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zh  --2020/3/29 10:20
 */
public class FileTest {

    @Test
    void testPath() {
        Path res = Paths.get("C:", "user");
        System.out.println(res);
    }

    @Test
    void testClassLoader() {
        URL res = this.getClass().getClassLoader().getResource("com/xiaobu");
        System.out.println(res);
    }

    @Test
    void testFileUtil() {
        Path parent = FileUtil.getDirectory("static", "user");
        FileUtil.getFile("avatar", parent);
    }

    @Test
    void testGetCurrentUser() {
        String user = System.getProperty("user");
        System.getProperties();
        System.out.println(user);
    }

    @Test
    void testLink() throws IOException {
        URL resource = BlogApplication.class.getClassLoader().getResource("");
        Files.createSymbolicLink(Paths.get(resource.getPath(),"static","webapp"), Paths.get("C:/webapp"));
    }
}
