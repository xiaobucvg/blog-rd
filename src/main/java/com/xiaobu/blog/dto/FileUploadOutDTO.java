package com.xiaobu.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 文件上传提示信息
 *
 * @author zh  --2020/4/4 9:44
 */
@Data
public class FileUploadOutDTO {
    private String url;  // 上传后的路径

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date uploadTime; // 上传时间

    private String originFileName; // 原始文件名

    private String fileName; // 上传后的文件名
}
