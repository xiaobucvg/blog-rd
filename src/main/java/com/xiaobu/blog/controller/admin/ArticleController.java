package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.common.Pageable;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.exception.ValidationException;
import com.xiaobu.blog.dto.ArticleInDTO;
import com.xiaobu.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器 - 后台
 *
 * @author zh  --2020/3/17 20:30
 */
@RestController
@RequestMapping("/admin/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Response postArticle(@RequestBody @Validated ArticleInDTO articleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        return articleService.saveArticle(articleDTO);
    }

    @PutMapping
    public Response putArticle(@RequestBody @Validated ArticleInDTO articleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        return articleService.updateArticle(articleDTO);
    }

    @GetMapping("/hot_articles/{count}")
    public Response getHotArticles(@PathVariable(required = false) Integer count) {
        if (count == null) {
            count = 10;
        }
        return articleService.getHostArticles(count);
    }

    @GetMapping
    public Response getArticles(@RequestBody @Validated Pageable pageable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        return articleService.getArticles(pageable);
    }

    @DeleteMapping("/{ids}")
    public Response deleteArticle(@PathVariable String ids) {
//        return articleService.deleteArticle(ids);
        return null;
    }

    @PostMapping("/{ids}")
    public Response topArticle(@PathVariable Long ids) {
//        return articleService.topArticles(ids);
        return null;
    }
}
