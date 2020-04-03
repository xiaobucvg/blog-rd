package com.xiaobu.blog.service;

import com.xiaobu.blog.common.page.Page;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.LogOutDTO;
import com.xiaobu.blog.exception.LogException;
import com.xiaobu.blog.mapper.LogMapper;
import com.xiaobu.blog.model.Log;
import com.xiaobu.blog.model.LogExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zh  --2020/3/29 16:11
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    /**
     * 记录操作
     */
    public void saveLog(Log log) throws LogException {
        try {
            int res = logMapper.insertSelective(log);
            if (res != 1) {
                // todo 记录日志
            }
        } catch (Exception e) {
            e.printStackTrace();
            // todo 记录日志
        }
    }

    /**
     * 分页获取日志
     */
    public Response getLogs(Pageable pageable) {
        // 1. 获取数量
        LogExample example = new LogExample();
        long count = logMapper.countByExample(example);

        // 2. 获取具体数据
        List<Log> logs = logMapper._selectLogBuPageable(pageable);
        // 3. 组装
        Page page = Page.createPage(pageable, count, this.convertLogBatch(logs));

        return Response.newSuccessInstance("获取日志记录成功", page);
    }

    private List<LogOutDTO> convertLogBatch(List<Log> logs) {
        List<LogOutDTO> res = new ArrayList<>();
        logs.forEach(log -> {
            res.add(new LogOutDTO().toModel(log));
        });
        return res;
    }
}
