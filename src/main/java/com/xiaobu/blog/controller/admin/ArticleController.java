package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.aspect.RequestJsonParamToObject;
import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.dto.ArticleInDTO;
import com.xiaobu.blog.exception.ValidationException;
import com.xiaobu.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器 - 后台
 *
 * @author zh  --2020/3/17 20:30
 */
@RestController("article_controller_admin")
@RequestMapping("/admin/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 保存/更新文章
     * /admin/articles POST
     */
    @PostMapping
    public Response postArticle(@RequestBody @Validated ArticleInDTO articleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        if (articleDTO.getId() == null) {
            return articleService.saveArticle(articleDTO);
        } else {
            return articleService.updateArticle(articleDTO);
        }
    }

    /**
     * 批量改变状态
     * /admin/articles?ids=""&statusCode=1 PUT
     */
    @PutMapping
    public Response changeArticleStatus(@RequestParam("ids") String ids, @RequestParam("statusCode") int statusCode) {
        return articleService.changeArticleStatus(ids, statusCode);
    }

    /**
     * 批量删除
     * /admin/articles?ids="" DELETE
     */
    @DeleteMapping
    public Response deleteArticle(@RequestParam("ids") String ids) {
        return articleService.changeArticleStatus(ids, Const.ArticleStatus.DELETED.getCode());
    }

    /**
     * 获取文章详细信息
     * /admin/articles/article?id=1
     */
    @GetMapping("/article")
    public Response getDetailArticle(@RequestParam Long id) {
        return articleService.getDetailArticle(id);
    }

    /**
     * 分页查询文章记录
     * /admin/articles?json={}&keywords=""
     */
    @GetMapping
    @RequestJsonParamToObject(Pageable.class)
    public Response searchArticle(
            @RequestParam("json") String json, Pageable pageable, BindingResult bindingResult,
            @RequestParam(value = "keywords", required = false) String keywords) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        // 没有关键字
        if (StringUtils.isEmpty(keywords)) {
            return articleService.getArticles(pageable);
        }
        // 有关键字
        return articleService.searchArticle(pageable, keywords);
    }
}
