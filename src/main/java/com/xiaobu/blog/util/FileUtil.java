package com.xiaobu.blog.util;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件存写工具
 *
 * @author zh  --2020/3/29 10:30
 */
public class FileUtil {

    private FileUtil() {
    }
    public static void getResource(){
        URL resource = FileUtil.class.getClassLoader().getResource("");
        System.out.println(resource);
    }
    /**
     * 获取目录
     * 如果没有则创建
     */
    public static Path getDirectory(String first, String... more) {
        if (StringUtils.isEmpty(first)) {
            throw new RuntimeException("获取的路径不能为空");
        }
        Path path = Paths.get(first, more);
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return path;
    }

    /**
     * 获取文件
     */
    public static Path getFile(String fileName, Path parent) {
        if (StringUtils.isEmpty(fileName)) {
            throw new RuntimeException("文件名不能空");
        }
        String parentPath = "";
        if (parent != null) {
            if (!Files.isDirectory(parent)) {
                throw new RuntimeException("只能在目录下创建文件");
            }
            parentPath = parent.toString();
        }
        Path file = Paths.get(parentPath, fileName);
        if (Files.notExists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return file;
    }

    /**
     * 写文件
     */
    public static void write(Path path, byte[] fileBytes) {
        try {
            Files.write(path, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入文件出现异常");
        }
    }

    public static void write(Path path, MultipartFile multipartFile) {
        FileUtil.class.getClassLoader().getResource(path.toString());
        try {
            Path res = Files.write(path, multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("写入文件出现异常");
        }
    }
}
