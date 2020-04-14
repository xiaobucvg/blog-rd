package com.xiaobu.blog.controller.admin;

import com.xiaobu.blog.aspect.annotation.PageableAutoCalculate;
import com.xiaobu.blog.aspect.annotation.RequestJsonParamToObject;
import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.ArticleInDTO;
import com.xiaobu.blog.service.ArticleService;
import com.xiaobu.blog.common.FileType;
import com.xiaobu.blog.validator.annotation.MultiPartFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * 文章控制器 - 后台
 *
 * @author zh  --2020/3/17 20:30
 */

@RestController("article_controller_admin")
@RequestMapping("/admin/articles")
@Validated
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 保存/更新文章
     * /admin/articles POST
     */
    @PostMapping
    public Response postArticle(@RequestBody @Valid ArticleInDTO articleDTO) {
        if (articleDTO.getId() == null) {
            return articleService.saveArticle(articleDTO);
        } else {
            return articleService.updateArticle(articleDTO);
        }
    }

    /**
     * 保存/更新 关于我文章
     * /admin/articles/about-article
     */
    @PostMapping("/about-article")
    public Response postAboutArticle(@RequestBody @Valid ArticleInDTO articleDTO) {
        if (articleDTO.getId() == null) {
            return articleService.saveAboutArticle(articleDTO);
        } else {
            return articleService.updateAboutArticle(articleDTO);
        }
    }

    /**
     * 保存/更新 友情链接文章
     * /admin/articles/link-article
     */
    @PostMapping("/link-article")
    public Response postLinkArticle(@RequestBody @Valid ArticleInDTO articleDTO) {
        if (articleDTO.getId() == null) {
            return articleService.saveLinkArticle(articleDTO);
        } else {
            return articleService.updateLinkArticle(articleDTO);
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
     * 获取文章详细信息
     * /admin/articles/article?id=1
     */
    @GetMapping("/article")
    public Response getDetailArticle(@RequestParam Long id) {
        return articleService.getDetailArticle(id);
    }

    /**
     * 分页查询文章记录
     * 只获取已发布和未发布的文章记录
     * /admin/articles?json={}&keywords=""
     */
    @GetMapping
    @RequestJsonParamToObject(Pageable.class)
    @PageableAutoCalculate
    public Response searchArticle(
            @RequestParam("json") String json, Pageable pageable,
            @RequestParam(value = "keywords", required = false) String keywords) {
        // 没有关键字
        if (StringUtils.isEmpty(keywords)) {
            return articleService.getArticles(pageable);
        }
        // 有关键字
        return articleService.searchArticle(pageable, keywords);
    }

    /**
     * 分页查询所有被删除的文章
     * /admin/articles/deleted-articles?json={}
     */
    @GetMapping("/deleted-articles")
    @RequestJsonParamToObject(Pageable.class)
    @PageableAutoCalculate
    public Response searchDeletedArticles(@RequestParam("json") String json, Pageable pageable) {
        return articleService.getDeletedArticles(pageable);
    }

    /**
     * 批量删除文章
     * /admin/articles DELETE
     */
    @DeleteMapping
    public Response deleteArticles(@RequestParam(required = false) String ids) {
        return articleService.deleteArticles(ids);
    }

    /**
     * 获取特殊文章记录
     * /admin/articles/special-articles
     */
    @GetMapping("/special-articles")
    public Response getSpecialArticles() {
        return articleService.getSpecialArticles();
    }

    /**
     * 上传图片
     * /admin/articles/image POST
     */
    @PostMapping("/image")
    public Response postImage(@RequestParam("img") @MultiPartFile(message = "上传的图片不符合要求", maxSize = Const.FILE_IMAGE_MAX_SIZE,type = FileType.IMAGE) MultipartFile multipartFile) {
        return articleService.postImage(multipartFile);
    }
}
