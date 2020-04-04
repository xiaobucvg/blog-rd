package com.xiaobu.blog.service;

import com.xiaobu.blog.aspect.annotation.Log;
import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.page.Page;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.dto.ArticleDetailOutDTO;
import com.xiaobu.blog.dto.ArticleInDTO;
import com.xiaobu.blog.dto.ArticleItemOutDTO;
import com.xiaobu.blog.dto.FileUploadOutDTO;
import com.xiaobu.blog.exception.ArticleException;
import com.xiaobu.blog.mapper.ArticleMapper;
import com.xiaobu.blog.mapper.TagMapper;
import com.xiaobu.blog.model.Article;
import com.xiaobu.blog.model.ArticleExample;
import com.xiaobu.blog.model.Tag;
import com.xiaobu.blog.model.TagExample;
import com.xiaobu.blog.model.wrapper.ArticleWithTag;
import com.xiaobu.blog.util.FileUploadUtil;
import com.xiaobu.blog.util.NetUtil;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    private FileUploadUtil fileUploadUtil;


    // ====================== 后台 ====================== //

    /**
     * 上传文章图片
     */
    public Response postImage(MultipartFile multipartFile) {
        try {
            String fileName = fileUploadUtil.uploadFile(multipartFile);
            String url = NetUtil.getServerAddress() + "/" + fileName;
            FileUploadOutDTO uploadOutDTO = new FileUploadOutDTO();
            uploadOutDTO.setOriginFileName(multipartFile.getOriginalFilename());
            uploadOutDTO.setFileName(fileName);
            uploadOutDTO.setUrl(url);
            uploadOutDTO.setUploadTime(new Date());
            return Response.newSuccessInstance("上传成功", uploadOutDTO);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ArticleException("上传图片失败");
        }
    }

    /**
     * 获取特殊文章记录（友情链接，关于我）
     */
    public Response getSpecialArticles() {
        ArticleExample example = new ArticleExample();
        Integer[] statusCode = new Integer[]{Const.ArticleStatus.LINK.getCode(), Const.ArticleStatus.ABOUT.getCode()};
        example.createCriteria().andStatusIn(Arrays.asList(statusCode));
        List<Article> articles = articleMapper.selectByExample(example);
        List<ArticleItemOutDTO> articleItemOutDTOS = this.convertArticleBatch(articles);
        return Response.newSuccessInstance("获取特殊文章成功", articleItemOutDTOS);
    }

    /**
     * 更新友情链接文章
     */
    public Response updateLinkArticle(ArticleInDTO articleInDTO) {
        // 检查
        if (articleInDTO.getId() == null) {
            throw new ArticleException("更新失败，更新文章时，ID不能为空");
        }
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andStatusEqualTo(Const.ArticleStatus.LINK.getCode());
        List<Article> articles = articleMapper.selectByExample(articleExample);
        if (articles == null || articles.size() == 0) {
            throw new ArticleException("更新失败，'友情链接'文章不存在");
        }
        Article article = articles.get(0);
        if (!Objects.equals(article.getId(), articleInDTO.getId())) {
            throw new ArticleException("更新失败，文章ID不匹配");
        }
        // 开始更新
        articleInDTO.setStatus(Const.ArticleStatus.LINK.getCode());
        ((ArticleService) AopContext.currentProxy()).updateArticle(articleInDTO);
        return this.updateArticle(articleInDTO);
    }

    /**
     * 新建友情链接文章
     */
    public Response saveLinkArticle(ArticleInDTO articleInDTO) {
        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusEqualTo(Const.ArticleStatus.LINK.getCode());
        List<Article> articles = articleMapper.selectByExample(example);
        // 可以创建
        if (articles == null || articles.isEmpty()) {
            articleInDTO.setStatus(Const.ArticleStatus.LINK.getCode());
            return ((ArticleService) AopContext.currentProxy()).saveArticle(articleInDTO);
        }
        throw new ArticleException("创建失败，'友情链接'已经存在");
    }

    /**
     * 新建关于我文章
     */
    public Response saveAboutArticle(ArticleInDTO articleInDTO) {
        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusEqualTo(Const.ArticleStatus.ABOUT.getCode());
        List<Article> articles = articleMapper.selectByExample(example);
        // 可以创建
        if (articles == null || articles.isEmpty()) {
            articleInDTO.setStatus(Const.ArticleStatus.ABOUT.getCode());
            return ((ArticleService) AopContext.currentProxy()).saveArticle(articleInDTO);
        }
        throw new ArticleException("创建失败，'关于我'已经存在");
    }

    /**
     * 更新关于我文章
     */
    public Response updateAboutArticle(ArticleInDTO articleInDTO) {
        // 检查
        if (articleInDTO.getId() == null) {
            throw new ArticleException("更新失败，更新文章时，ID不能为空");
        }
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andStatusEqualTo(Const.ArticleStatus.ABOUT.getCode());
        List<Article> articles = articleMapper.selectByExample(articleExample);
        if (articles == null || articles.size() == 0) {
            throw new ArticleException("更新失败，'关于我'文章不存在");
        }
        Article article = articles.get(0);
        if (!Objects.equals(article.getId(), articleInDTO.getId())) {
            throw new ArticleException("更新失败，文章ID不匹配");
        }
        // 开始更新
        articleInDTO.setStatus(Const.ArticleStatus.ABOUT.getCode());
        return ((ArticleService) AopContext.currentProxy()).updateArticle(articleInDTO);
    }

    /**
     * 更新文章
     * - 检查文章状态
     * - 处理 tag
     * - 更新文章
     * - 处理中间表
     */
    @Log("更新了文章")
    @Transactional
    public Response updateArticle(ArticleInDTO articleInDTO) {
        // 1. 判断是否能够更新
        if (articleInDTO.getId() == null) {
            throw new ArticleException("更新文章时，ID不能为空");
        }
        ArticleWithTag articleWithTag = articleInDTO.toModel();
        Article article = articleMapper.selectByPrimaryKey(articleWithTag.getArticle().getId());
        if (article == null) {
            throw new ArticleException("更新失败，ID 为 " + articleWithTag.getArticle().getId() + " 的文章不存在");
        }
        if (Const.ArticleStatus.DELETED.getCode() == articleWithTag.getArticle().getStatus()) {
            throw new ArticleException("更新失败，ID 为 " + articleWithTag.getArticle().getId() + " 的文章已经被删除,如果要修改,请先将其恢复");
        }

        List<Long> ids = new ArrayList<>();
        ids.add(articleWithTag.getArticle().getId());
        if (this.existsArticle(articleWithTag.getArticle().getTitle(), ids)) {
            throw new ArticleException("更新文章失败，标题已经存在");
        }

        // 2. 开始更新
        Set<Tag> tagSet = processTags(articleWithTag.getTags());
        articleWithTag.setTags(tagSet);
        // 不能变动的属性
        articleWithTag.getArticle().setReading(null);
        articleWithTag.getArticle().setStatus(null);
        articleWithTag.getArticle().setCreateTime(null);
        articleWithTag.getArticle().setUpdateTime(new Date());
        try {
            int res = articleMapper.updateByPrimaryKeySelective(articleWithTag.getArticle());
            if (res != 1) {
                throw new ArticleException("更新文章发生错误");
            }
            articleMapper._deleteAllTags(articleWithTag.getArticle());
            articleMapper._insertArticleTag(articleWithTag);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ArticleException("更新文章发生错误");
        }
        return Response.newSuccessInstance("更新文章成功");
    }

    /**
     * 新建文章
     * - 处理 tag
     * - 插入 article
     * - 处理中间表
     */
    @Log("创建了新文章")
    @Transactional
    public Response saveArticle(ArticleInDTO articleInDTO) {
        if (this.existsArticle(articleInDTO.getTitle(), null)) {
            throw new ArticleException("新建文章失败，标题已经存在");
        }
        // 必须去掉 ID
        articleInDTO.setId(null);
        ArticleWithTag articleWithTag = articleInDTO.toModel();
        Set<Tag> tags = articleWithTag.getTags();
        articleWithTag.setTags(processTags(tags));
        try {
            int res = articleMapper._insertArticleSelective(articleWithTag.getArticle());
            if (res != 1) {
                throw new ArticleException("新建文章失败，没有文章被创建");
            }
            articleMapper._insertArticleTag(articleWithTag);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ArticleException("新建文章发生异常");
        }

        return Response.newSuccessInstance("新建文章成功");
    }

    /**
     * 检查该标题的文章是否存在
     *
     * @param title 要检查的标题
     * @param ids   要排除检查的文章 ID
     */
    private boolean existsArticle(String title, List<Long> ids) {
        ArticleExample example = new ArticleExample();
        ArticleExample.Criteria criteria = example.createCriteria().andTitleEqualTo(title);
        if (ids != null && ids.size() > 0) {
            criteria.andIdNotIn(ids);
        }
        List<Article> articles = articleMapper.selectByExample(example);
        return articles != null && articles.size() > 0;
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
                try {
                    tagMapper._insertTagSelective(tag);
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ArticleException("处理标签发生错误");
                }
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
        return Response.newSuccessInstance("获取热门文章记录成功", convertArticleBatch(articles));
    }

    /**
     * 根据分页信息获取文章
     * - 获取正常状态的文章总数量，计算分页
     * - 将结果用分页进行包装
     */
    public Response getArticles(Pageable pageable) {

        // 2. 获取具体内容
        List<Article> articles = articleMapper._selectArticles(pageable);

        // 1. 计算数量
        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusNotEqualTo(Const.ArticleStatus.DELETED.getCode());
        long articleCounts = articleMapper.countByExample(example);

        // 3. 封装返回
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
    @Log("修改了文章状态")
    @Transactional
    public Response changeArticleStatus(String ids, int status) {
        if (!this.checkStatusCode(status)) {
            throw new ArticleException("修改文章状态失败要修改的状态不存在");
        }
        try {
            articleMapper._updateArticleStatusByIds(this.splitIds(ids), status);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ArticleException("修改文章状态发生错误");
        }

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
                throw new ArticleException("ID 解析出错,请使用 ',' 将 ID 分割,并且不能出现其他字符");
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
        // 1. 查询数量
        long articleCounts = articleMapper._countArticlesByKeywords(keywords);

        // 2. 获取具体内容
        List<Article> articles = articleMapper._selectByKeywords(pageable, keywords);
        List<ArticleItemOutDTO> data = this.convertArticleBatch(articles);

        //3. 封装返回
        Page page = Page.createPage(pageable, articleCounts, data);

        return Response.newSuccessInstance("搜索完成", page);
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

    /**
     * 分页获取被删除的文章
     */
    public Response getDeletedArticles(Pageable pageable) {
        // 1. 获取数量
        ArticleExample articleExample = new ArticleExample();
        articleExample.createCriteria().andStatusEqualTo(Const.ArticleStatus.DELETED.getCode());
        long count = articleMapper.countByExample(articleExample);
        // 2. 查询具体数据
        List<Article> articles = articleMapper._selectDeletedArticles(pageable);
        List<ArticleItemOutDTO> articleItemOutDTOS = convertArticleBatch(articles);
        // 3. 封装返回
        Page page = Page.createPage(pageable, count, articleItemOutDTOS);
        return Response.newSuccessInstance("获取已删除的文章记录成功", page);
    }

    /**
     * 批量删除删除状态的文章
     * 如果没有指定 ids ，删除全部删除状态的文章
     * - 删除中间表相关数据
     * - 删除文章表
     */
    @Log("删除了文章")
    @Transactional
    public Response deleteArticles(String ids) {
        // 查出所有符合删除条件的文章
        ArticleExample example = new ArticleExample();
        ArticleExample.Criteria criteria = example.createCriteria().andStatusEqualTo(Const.ArticleStatus.DELETED.getCode());
        if (ids != null) {
            List idList = this.splitIds(ids);
            criteria.andIdIn(idList);
        }
        List<Article> articles = articleMapper.selectByExample(example);
        if (articles != null && articles.size() > 0) {
            try {
                // 处理中间表
                articleMapper._deleteTagsByArticles(articles);

                // 处理文章
                articleMapper.deleteByExample(example);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ArticleException("删除文章发生错误");
            }

        }
        return Response.newSuccessInstance("删除了" + articles.size() + "篇文章");
    }

    // ====================== 前台 ====================== //

    /**
     * 分页获取已经发布的文章
     */
    public Response getPublishedArticles(Pageable pageable) {

        // 2. 获取具体内容
        List<Article> articles = articleMapper._selectPublishedArticlesByPage(pageable);

        // 1. 统计数量
        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusIn(Arrays.asList(Const.ArticleStatus.PUBLISHED.getCode()));
        long articleCounts = articleMapper.countByExample(example);

        // 3. 创建分页
        Page page = Page.createPage(pageable, articleCounts, convertArticleBatch(articles));

        return Response.newSuccessInstance("获取成功", page);

    }


    /**
     * 分页搜索已经发布的文章
     */
    public Response searchPublishedArticles(Pageable pageable, String keywords) {

        // 1. 获取数量
        long articleCounts = articleMapper._countPublisedArticlesByKeywords(keywords);

        // 2. 查询具体内容
        List<Article> articles = articleMapper._selectPublishedArticlesByKeywords(pageable, keywords);
        List<ArticleItemOutDTO> data = this.convertArticleBatch(articles);

        // 3. 封装返回
        Page page = Page.createPage(pageable, articleCounts, data);
        return Response.newSuccessInstance("搜索完成", page);
    }

    /**
     * 归档查询
     * 1. 先把所有月份查出来
     * 2. 根据月份查询每个月的文章
     * 3. 将其组装
     */

    public Response archiveArticles(Pageable pageable) {
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
        // 1. 获取数量
        long count = tagMapper._selectArticlesCount(tagId);

        // 2. 查询具体数据
        List<Article> articles = articleMapper._selectPublishedArticlesByTag(tagId, pageable);
        List<ArticleItemOutDTO> articleItemOutDTOS = convertArticleBatch(articles);
        // 3. 封装返回
        Page page = Page.createPage(pageable, count, articleItemOutDTOS);

        return Response.newSuccessInstance("获取标签ID为" + tagId + "的文章记录成功", page);
    }


    /**
     * 获取置顶文章记录
     */
    public Response getTopArticles() {
        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusEqualTo(Const.ArticleStatus.TOP.getCode());
        List<Article> articles = articleMapper.selectByExample(example);
        List<ArticleItemOutDTO> articleItemOutDTOS = convertArticleBatch(articles);
        return Response.newSuccessInstance("获取置顶文章成功", articleItemOutDTOS);
    }

    /**
     * 获取友情链接文章
     * /articles/link-article
     */
    public Response getLinkArticle() {
        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusEqualTo(Const.ArticleStatus.LINK.getCode());
        List<Article> articles = articleMapper.selectByExample(example);
        if (articles != null && !articles.isEmpty()) {
            Article article = articles.get(0);
            ArticleWithTag articleWithTag = new ArticleWithTag();
            articleWithTag.setArticle(article);
            ArticleDetailOutDTO articleDetailOutDTO = new ArticleDetailOutDTO().toModel(articleWithTag);
            return Response.newSuccessInstance("获取'友情链接'文章成功", articleDetailOutDTO);
        }
        throw new ArticleException("还没有创建'友情链接'文章");
    }

    /**
     * 获取关于我文章
     */
    public Response getAboutArticle() {
        ArticleExample example = new ArticleExample();
        example.createCriteria().andStatusEqualTo(Const.ArticleStatus.ABOUT.getCode());
        List<Article> articles = articleMapper.selectByExample(example);
        if (articles != null && !articles.isEmpty()) {
            Article article = articles.get(0);
            ArticleWithTag articleWithTag = new ArticleWithTag();
            articleWithTag.setArticle(article);
            ArticleDetailOutDTO articleDetailOutDTO = new ArticleDetailOutDTO().toModel(articleWithTag);
            return Response.newSuccessInstance("获取'关于我'文章成功", articleDetailOutDTO);
        }
        throw new ArticleException("还没有创建'关于我'文章");

    }


}
