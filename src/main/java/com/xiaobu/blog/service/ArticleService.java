package com.xiaobu.blog.service;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.page.Page;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.dto.ArticleDetailOutDTO;
import com.xiaobu.blog.dto.ArticleInDTO;
import com.xiaobu.blog.dto.ArticleItemOutDTO;
import com.xiaobu.blog.exception.ArticleException;
import com.xiaobu.blog.mapper.ArticleMapper;
import com.xiaobu.blog.mapper.TagMapper;
import com.xiaobu.blog.model.Article;
import com.xiaobu.blog.model.ArticleExample;
import com.xiaobu.blog.model.Tag;
import com.xiaobu.blog.model.TagExample;
import com.xiaobu.blog.model.wrapper.ArticleWithTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
        return Response.newSuccessInstance("获取热门文章记录成功!", convertArticleBatch(articles));
    }

    /**
     * 根据分页信息获取文章
     * - 获取正常状态的文章总数量，计算分页
     * - 将结果用分页进行包装
     */
    public Response getArticles(Pageable pageable) {
        pageable.calculate();

        List<Article> articles = articleMapper._selectArticles(pageable);

        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusNotEqualTo(Const.ArticleStatus.DELETED.getCode());
        long articleCounts = articleMapper.countByExample(example);

        Page page = Page.createPage(pageable, articleCounts, convertArticleBatch(articles));

        return Response.newSuccessInstance("获取文章记录成功", page);
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
     * 分割并检验字符串形式的 ID 是否能够变成 int 类型的集合
     *
     * @return idList
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

    /**
     * 分页搜索文章
     */
    public Response searchArticle(Pageable pageable, String keywords) {
        pageable.calculate();
        long articleCounts = articleMapper._countArticlesByKeywords(keywords);

        List<Article> articles = articleMapper._selectByKeywords(pageable, keywords);

        List<ArticleItemOutDTO> data = this.convertArticleBatch(articles);

        Page page = Page.createPage(pageable, articleCounts, data);

        return Response.newSuccessInstance("搜索完成!", page);
    }

    /**
     * 获取文章详细信息
     */
    public Response getDetailArticle(long id) {
        ArticleWithTag articleWithTag = articleMapper._selectArticleWithTag(id);
        if (articleWithTag != null) {
            ArticleDetailOutDTO articleDetailOutDTO = new ArticleDetailOutDTO().toModel(articleWithTag);
            this.addReading(id);
            return Response.newSuccessInstance("获取详细信息成功", articleDetailOutDTO);
        }
        throw new ArticleException("没有找到文章");
    }

    /**
     * 异步的增加访问量
     */
    @Async
    void addReading(long id) {
        articleMapper._addReading(id);
    }

    // ====================== 前台 ====================== //

    /**
     * 分页获取已经发布的文章
     */
    public Response getPublishedArticles(Pageable pageable) {
        pageable.calculate();

        List<Article> articles = articleMapper._selectPublishedArticlesByPage(pageable);

        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusIn(Arrays.asList(Const.ArticleStatus.PUBLISHED.getCode()));
        long articleCounts = articleMapper.countByExample(example);

        Page page = Page.createPage(pageable, articleCounts, convertArticleBatch(articles));

        return Response.newSuccessInstance("获取成功", page);

    }


    /**
     * 分页搜索已经发布的文章
     */
    public Response searchPublishedArticles(Pageable pageable, String keywords) {
        pageable.calculate();

        long articleCounts = articleMapper._countPublisedArticlesByKeywords(keywords);

        List<Article> articles = articleMapper._selectPublishedArticlesByKeywords(pageable, keywords);

        List<ArticleItemOutDTO> data = this.convertArticleBatch(articles);

        Page page = Page.createPage(pageable, articleCounts, data);

        return Response.newSuccessInstance("搜索完成!", page);
    }

    /**
     * 归档查询
     * 1. 先把所有月份查出来
     * 2. 根据月份查询每个月的文章
     * 3. 将其组装
     */

    public Response archiveArticles(Pageable pageable) {
        pageable.calculate();

        // 查询总的归档数
        long archiveCount = articleMapper._selectArchiveCounts();

        // 分页查询归档
        List<String> months = articleMapper._selectMonth(pageable);

        if (months == null || months.size() == 0) {
            throw new ArticleException("还没有归档哦~");
        }

        // 遍历归档查询具体文章
        Map<String, List<ArticleItemOutDTO>> res = new LinkedHashMap<>();

        months.forEach(month -> {
            List<Article> articles = articleMapper._selectArticlesByMonth(month);
            res.put(month, convertArticleBatch(articles));
        });

        Page page = Page.createPage(pageable, archiveCount, res);

        return Response.newSuccessInstance("获取归档记录成功", page);
    }


    /**
     * 分页获取标签下的文章
     */
    public Response getPublishedArticlesByTagID(Pageable pageable, Long tagId) {
        pageable.calculate();
        // 1. 获取数量
        long count = tagMapper._selectArticlesCount(tagId);

        // 2. 查询具体数据
        List<Article> articles = articleMapper._selectPublishedArticlesByTag(tagId, pageable);
        List<ArticleItemOutDTO> articleItemOutDTOS = convertArticleBatch(articles);
        // 3. 封装返回
        Page page = Page.createPage(pageable, count, articleItemOutDTOS);

        return Response.newSuccessInstance("获取标签ID为" + tagId + "的文章记录成功", page);
    }
}
