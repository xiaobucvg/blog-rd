package com.xiaobu.blog.controller;

import com.xiaobu.blog.aspect.annotation.PageableAutoCalculate;
import com.xiaobu.blog.aspect.annotation.RequestJsonParamToObject;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标签控制器 - 前台
 *
 * @author zh  --2020/3/22 16:44
 */
@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    /**
     * 分页获取标签
     * /tags
     */
    @GetMapping
    @RequestJsonParamToObject(Pageable.class)
    @PageableAutoCalculate
    public Response getTags(@RequestParam("json") String json, Pageable pageable) {
        return tagService.getTags(pageable);
    }
}
