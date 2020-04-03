package com.xiaobu.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * 文件上传工具
 *
 * @author zh  --2020/4/2 20:38
 */
@Component
public class FileUploadUtil {

    @Value("${custom.file-upload}")
    private String fileUpload = System.getProperty("user.dir");

    public void uploadFile(MultipartFile file) throws IOException {
        this.uploadFile(file.getBytes(), UUID.randomUUID() + file.getOriginalFilename());
    }

    public void uploadFile(byte[] bytes, String fileName) throws IOException {
        Path path = Paths.get(fileUpload);
        // 如果目录不存在
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        // 如果不是目录
        else if (!Files.isDirectory(path)) {
            throw new IOException("只能将文件上传到目录中");
        }
        if (!Files.isWritable(path)) {
            throw new IOException("没有写入权限");
        }
        Files.write(Paths.get(path.toString(), fileName), bytes);
    }
}
