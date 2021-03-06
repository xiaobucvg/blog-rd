package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.aspect.annotation.JsonParam;
import com.xiaobu.blog.aspect.util.PageableAfterProcessor;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 日志控制器
 *
 * @author zh  --2020/3/29 16:22
 */
@RestController
@RequestMapping("/admin/logs")
public class LogController {

    @Autowired
    private LogService logService;

    /**
     * 分页获取日志
     * /logs?json={}
     */
    @GetMapping
    public Response getLogs(@RequestParam @JsonParam(value = Pageable.class, afterProcessor = PageableAfterProcessor.class) String json, Pageable pageable, BindingResult bindingResult) {
        return logService.getLogs(pageable);
    }

}
