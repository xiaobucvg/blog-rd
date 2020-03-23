package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.aspect.RequestJsonParamToObject;
import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.exception.ValidationException;
import com.xiaobu.blog.dto.ArticleInDTO;
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

    @PostMapping
    public Response postArticle(@RequestBody @Validated ArticleInDTO articleDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        if(articleDTO.getId() == null){
            return articleService.saveArticle(articleDTO);
        }
        else {
            return articleService.updateArticle(articleDTO);
        }
    }

    /**
     * 批量改变状态
     */
    @PutMapping("/{ids}")
    public Response changeArticleStatus(@PathVariable("ids") String ids, @RequestParam("statusCode") int statusCode) {
        return articleService.changeArticleStatus(ids, statusCode);
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/{ids}")
    public Response deleteArticle(@PathVariable("ids") String ids) {
        return articleService.changeArticleStatus(ids, Const.ArticleStatus.DELETED.getCode());
    }


    @GetMapping("/hot-articles")
    public Response getHotArticles(@RequestParam(value = "count", required = false) Integer count) {
        if (count == null) {
            count = 10;
        }
        return articleService.getHostArticles(count);
    }


    @GetMapping("/{id}")
    public Response getDetailArticle(@PathVariable Long id){
        return articleService.getDetailArticle(id);
    }

    /**
     * 分页查询
     */
    @GetMapping
    @RequestJsonParamToObject(Pageable.class)
    public Response searchArticle(@RequestParam("json") String json, Pageable pageable, BindingResult bindingResult, @RequestParam(value = "keywords",required = false) String keywords) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult, "ArticleInDTO 字段验证失败");
        }
        // 没有关键字
        if(StringUtils.isEmpty(keywords)){
            return articleService.getArticles(pageable);
        }
        // 有关键字
        return articleService.searchArticle(pageable, keywords);
    }
}
