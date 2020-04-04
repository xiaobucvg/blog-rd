package com.xiaobu.blog.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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

    public String uploadFile(MultipartFile file, String dir) throws IOException {
        if (StringUtils.isEmpty(dir)) {
            throw new IOException("上传的目录不能为空");
        }
        Path rootPath = this.generateRootDir();
        Path dirPath = Paths.get(rootPath.toString(), dir);
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        } else if (!Files.isDirectory(dirPath)) {
            throw new IOException("上传的路径不是目录");
        }
        if (!Files.isWritable(dirPath)) {
            throw new IOException("没有写入权限");
        }
        return this.uploadFile(file.getBytes(), dir + "/" + UUID.randomUUID() + file.getOriginalFilename());
    }

    public String uploadFile(MultipartFile file) throws IOException {
        return this.uploadFile(file.getBytes(), UUID.randomUUID() + file.getOriginalFilename());
    }

    public String uploadFile(byte[] bytes, String fileName) throws IOException {
        Path path = this.generateRootDir();
        Files.write(Paths.get(path.toString(), fileName), bytes);
        return fileName;
    }

    /**
     * 生成上传目录
     */
    public Path generateRootDir() throws IOException {
        Path path = Paths.get(fileUpload);
        // 如果目录不存在
        if (Files.notExists(path)) {
            Files.createDirectories(path);
        }
        // 如果不是目录
        else if (!Files.isDirectory(path)) {
            throw new IOException("上传路径已经存在，但不是一个目录");
        }
        if (!Files.isWritable(path)) {
            throw new IOException("上传目录没有写入权限");
        }
        return path;
    }
}
