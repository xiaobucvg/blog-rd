package com.xiaobu.blog.service;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.Pageable;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.exception.ArticleException;
import com.xiaobu.blog.dto.ArticleInDTO;
import com.xiaobu.blog.dto.ArticleItemOutDTO;
import com.xiaobu.blog.mapper.ArticleMapper;
import com.xiaobu.blog.mapper.TagMapper;
import com.xiaobu.blog.model.Article;
import com.xiaobu.blog.model.Tag;
import com.xiaobu.blog.model.TagExample;
import com.xiaobu.blog.model.wrapper.ArticleWithTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author zh  --2020/3/17 20:53
 */
@Service
public class ArticleService {

    @Autowired
    private TagMapper tagMapper;
    @Autowired
    private ArticleMapper articleMapper;

    /**
     * 更新文章
     * - 检查文章状态
     * - 处理 tag
     * - 更新文章
     * - 处理中间表
     */
    @Transactional
    public Response updateArticle(ArticleInDTO articleInDTO) {
        ArticleWithTag articleWithTag = articleInDTO.toModel();
        Article article = articleMapper.selectByPrimaryKey(articleWithTag.getArticle().getId());
        if (article == null) {
            throw new ArticleException("ID 为 " + articleWithTag.getArticle().getId() + " 的文章不存在!");
        }
        if (Const.ArticleStatus.DELETED.getCode() == articleWithTag.getArticle().getStatus()) {
            throw new ArticleException("ID 为 " + articleWithTag.getArticle().getId() + " 的文章已经被删除,如果要修改,请先将其恢复!");
        }
        Set<Tag> tagSet = processTags(articleWithTag.getTags());
        articleWithTag.setTags(tagSet);
        // 不需要变动的属性
        articleWithTag.getArticle().setReading(null);
        articleWithTag.getArticle().setStatus(null);
        articleWithTag.getArticle().setCreateTime(null);
        articleWithTag.getArticle().setUpdateTime(new Date());

        articleMapper.updateByPrimaryKeySelective(articleWithTag.getArticle());

        articleMapper._deleteAllTags(articleWithTag.getArticle());

        articleMapper._insertArticleTag(articleWithTag);

        return Response.newSuccessInstance("更新文章成功!");
    }

    /**
     * 新建文章
     * - 处理 tag
     * - 插入 article
     * - 处理中间表
     */
    @Transactional
    public Response saveArticle(ArticleInDTO articleInDTO) {
        // 必须去掉 ID
        articleInDTO.setId(null);
        ArticleWithTag articleWithTag = articleInDTO.toModel();
        Set<Tag> tags = articleWithTag.getTags();
        articleWithTag.setTags(processTags(tags));
        articleMapper._insertArticleSelective(articleWithTag.getArticle());
        articleMapper._insertArticleTag(articleWithTag);
        return Response.newSuccessInstance("新建文章成功!");
    }

    /**
     * 处理 tag
     * 将不存在的 Tag 全部保存
     *
     * @return newTagSet 带有 ID 的 Tag 集合
     */
    private Set<Tag> processTags(Set<Tag> tags) {

        Set<Tag> newTagSet = new HashSet<>();

        tags.forEach(tag -> {
            TagExample tagExample = new TagExample();
            tagExample.createCriteria().andNameEqualTo(tag.getName());
            List<Tag> tagResList = tagMapper.selectByExample(tagExample);
            if (tagResList.isEmpty()) {
                tagMapper._insertTagSelective(tag);
                newTagSet.add(tag);
            } else {
                newTagSet.add(tagResList.get(0));
            }
        });

        return newTagSet;
    }


    /**
     * 获取阅读量最高的前 count 条文章记录
     */
    public Response getHostArticles(int count) {
        List<Article> articles = articleMapper._selectHotArticles(count);
        return Response.newSuccessInstance("获取文章记录成功!", convertArticleBatch(articles));
    }

    /**
     * 根据分页信息获取文章
     */
    public Response getArticles(Pageable pageable) {
        pageable.calculate();

        List<Article> articles = articleMapper._selectArticles(pageable);
        return Response.newSuccessInstance("获取文章记录成功", convertArticleBatch(articles));
    }


    private List<ArticleItemOutDTO> convertArticleBatch(List<Article> articles) {
        List<ArticleItemOutDTO> newArticleList = new ArrayList<>();

        articles.forEach(article -> {
            newArticleList.add(new ArticleItemOutDTO().toModel(article));
        });

        return newArticleList;
    }

    /**
     * 批量修改文章状态
     * - 检查状态值
     * - 分割 ID
     * - 验证 ID 合法性
     * - 文章改变状态
     */
    public Response changeArticleStatus(String ids, int status) {
        if (!this.checkStatusCode(status)) {
            throw new ArticleException("修改文章状态失败!要修改的状态不存在");
        }
        articleMapper._updateArticleStatusByIds(this.splitIds(ids), status);
        return Response.newSuccessInstance("修改状态成功");
    }

    /**
     * 分割并检验字符串形式的 ID
     */
    private List<String> splitIds(String ids) {
        String[] idArray = ids.split(",");
        List<String> idList = Arrays.stream(idArray).collect(Collectors.toList());
        Pattern pattern = Pattern.compile("[0-9]*");
        idList.forEach(id -> {
            if (!pattern.matcher(id).matches()) {
                throw new ArticleException("ID 解析出错,请使用 ',' 将 ID 分割,并且不能出现其他字符!");
            }
        });
        return idList;
    }

    /**
     * 检查状态是否存在
     */
    private boolean checkStatusCode(int code) {
        for (Const.ArticleStatus value : Const.ArticleStatus.values()) {
            if (value.getCode() == code) {
                return true;
            }
        }
        return false;
    }
}
