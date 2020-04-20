package com.xiaobu.blog.controller.client;

import com.xiaobu.blog.aspect.annotation.JsonParam;
import com.xiaobu.blog.aspect.util.PageableAfterProcessor;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
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
    public Response searchArticle(
            @RequestParam("json") @JsonParam(value = Pageable.class, afterProcessor = PageableAfterProcessor.class) String json, Pageable pageable,
            @RequestParam(value = "keywords", required = false) String keywords,
            @RequestParam(value = "tagid", required = false) Long tagid
    ) {

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
    public Response archiveArticle(@RequestParam("json") @JsonParam(value = Pageable.class, afterProcessor = PageableAfterProcessor.class) String json, Pageable pageable) {
        return articleService.archiveArticles(pageable);
    }

    /**
     * 获取置顶文章
     * /articles/top-articles
     */
    @GetMapping("/top-articles")
    public Response getTopArticles() {
        return articleService.getTopArticles();
    }

    /**
     * 获取友情链接文章
     * /articles/link-article
     */
    @GetMapping("/link-article")
    public Response getLinkArticle() {
        return articleService.getLinkArticle();
    }

    /**
     * 获取关于我文章
     * /articles/about-articles
     */
    @GetMapping("/about-article")
    public Response getAboutArticle() {
        return articleService.getAboutArticle();
    }
}
