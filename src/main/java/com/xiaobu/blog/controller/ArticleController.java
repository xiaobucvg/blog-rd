package com.xiaobu.blog.controller;

import com.xiaobu.blog.aspect.annotation.PageableAutoCalculate;
import com.xiaobu.blog.aspect.annotation.RequestJsonParamToObject;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.exception.ValidationException;
import com.xiaobu.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章控制器 - 前台
 *
 * @author zh  --2020/3/22 9:28
 */
@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 获取文章详细数据
     * /articles/article?id=1
     */
    @GetMapping("/article")
    public Response getDetailArticle(@RequestParam Long id) {
        return articleService.getDetailArticle(id);
    }


    /**
     * 获取热门文章
     * /articles/hot-articles?count=10
     */
    @GetMapping("/hot-articles")
    public Response getHotArticles(@RequestParam(value = "count", required = false) Integer count) {
        if (count == null) {
            count = 10;
        }
        return articleService.getHostArticles(count);
    }


    /**
     * 分页查询已经发布的文章
     * 如果同时有关键字和标签ID，只查询关键字，只有单独出现标签ID，才查询标签ID
     * /articles?json={}&keywords=""&tagid=1
     */
    @GetMapping
    @RequestJsonParamToObject(Pageable.class)
    @PageableAutoCalculate
    public Response searchArticle(
            @RequestParam("json") String json, Pageable pageable, BindingResult bindingResult,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "tagid", required = false) Long tagid
    ) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        // 没有关键字
        if (StringUtils.isEmpty(keywords)) {
            if (tagid == null) {
                return articleService.getPublishedArticles(pageable);
            }
            return articleService.getPublishedArticlesByTagID(pageable, tagid);
        }
        // 有关键字
        return articleService.searchPublishedArticles(pageable, keywords);
    }

    /**
     * 分页归档查询
     * /articles/archives?json={}
     */
    @GetMapping("/archives")
    @RequestJsonParamToObject(Pageable.class)
    @PageableAutoCalculate
    public Response archiveArticle(@RequestParam("json") String json, Pageable pageable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        return articleService.archiveArticles(pageable);
    }
}
