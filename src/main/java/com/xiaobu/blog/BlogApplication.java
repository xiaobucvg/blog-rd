package com.xiaobu.blog;

import com.xiaobu.blog.util.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.io.IOException;

@Slf4j
@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.xiaobu.blog.mapper")
public class BlogApplication implements ApplicationListener {

    @Autowired
    private FileUploadUtil fileUploadUtil;

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class,args);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            try {
                fileUploadUtil.generateRootDir();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("系统启动失败：文件上传目录创建失败");
                System.exit(-1);
            }
        }
    }
}
