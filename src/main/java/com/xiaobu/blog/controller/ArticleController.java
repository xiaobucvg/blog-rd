package com.xiaobu.blog.controller;

import com.xiaobu.blog.aspect.RequestJsonParamToObject;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.exception.ValidationException;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author zh  --2020/3/22 9:28
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 分页查询
     */
    @GetMapping
    @RequestJsonParamToObject(Pageable.class)
    public Response searchArticle(@RequestParam("json") String json, Pageable pageable, BindingResult bindingResult, @RequestParam(value = "keywords", required = false) String keywords) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        // 没有关键字
        if (StringUtils.isEmpty(keywords)) {
            return articleService.getPublishedArticles(pageable);
        }
        // 有关键字
        return articleService.searchPublishedArticles(pageable, keywords);
    }

    /**
     * 分页归档查询
     */
    @GetMapping("/archives")
    @RequestJsonParamToObject(Pageable.class)
    public Response archiveArticle(@RequestParam("json") String json, Pageable pageable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        return articleService.archiveArticles(pageable);
    }

    /**
     * 分页获取某个标签下的文章
     */
    @GetMapping("/{tagid}")
    @RequestJsonParamToObject(Pageable.class)
    public Response getTagArticles(@RequestParam("json") String json, Pageable pageable, BindingResult bindingResult, @PathVariable("tagid") Long tagid) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        return articleService.getTagServices(pageable,tagid);
    }
}
