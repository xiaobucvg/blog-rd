package com.xiaobu.blog.dto;

import com.xiaobu.blog.model.Log;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * @author zh  --2020/3/29 16:31
 */
@Data
public class LogOutDTO implements Convert<Log>{
    private Long id;

    private String ip;

    private String msg;

    private Date createTime;

    private Date updateTime;

    @Override
    public LogOutDTO toModel(Log log) {
        BeanUtils.copyProperties(log,this);
        return this;
    }
}
