package com.xiaobu.blog.service;

import com.xiaobu.blog.common.page.Page;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.LogOutDTO;
import com.xiaobu.blog.mapper.LogMapper;
import com.xiaobu.blog.model.Log;
import com.xiaobu.blog.model.LogExample;
import com.xiaobu.blog.util.NetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zh  --2020/3/29 16:11
 */
@Service
@Slf4j()
public class LogService {

    @Autowired
    private LogMapper logMapper;

    @Async
    public void saveLog(String msg) {
        Log log = new Log();
        log.setMsg(msg);
        this.saveLog(log);
    }

    /**
     * 记录操作
     */
    public void saveLog(Log logObj) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String ip = NetUtil.getIpAddress(request);
        logObj.setIp(ip);
        logObj.setUpdateTime(new Date());
        logObj.setCreateTime(new Date());
        try {
            int res = logMapper.insertSelective(logObj);
            if (res != 1) {
                log.error("记录操作失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("记录操作失败");
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
