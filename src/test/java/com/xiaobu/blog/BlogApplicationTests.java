package com.xiaobu.blog;

import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.common.Response;
import com.xiaobu.blog.common.page.Sort;
import com.xiaobu.blog.dto.ArticleInDTO;
import com.xiaobu.blog.mapper.ArticleMapper;
import com.xiaobu.blog.model.Article;
import com.xiaobu.blog.model.ArticleExample;
import com.xiaobu.blog.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootTest
class BlogApplicationTests {

    @Test
    void contextLoads() {

    }

    @Autowired
    private ArticleMapper articleMapper;

    @Test
    void testMyBatis() {
        Article res = articleMapper.selectByPrimaryKey(1L);
    }

    @Test
    void testMyBatisInsert() {
        Article article = new Article();
        article.setAbstractInfo("文章摘要");
        article.setContent("文章内容");
        article.setTitle("文章标题");
        ArticleExample articleExample = new ArticleExample();
        articleMapper.insertSelective(article);
    }

    @Test
    void test_Insert() {
        Article article = new Article();
        article.setAbstractInfo("文章摘要");
        article.setContent("文章内容");
        article.setTitle("文章标题2");
        articleMapper._insertArticleSelective(article);
        System.out.println("新插入文章ID:" + article.getId());
    }

    @Autowired
    private ArticleService articleService;

    @Test
    void testNewArticle() {

        String[] tags = {"标签1", "标签2", "标签3"};

        ArticleInDTO articleInDTO = new ArticleInDTO();

        articleInDTO.setContent("文章内容");
        articleInDTO.setTitle("文章标题5");
        articleInDTO.setTags(Arrays.stream(tags).collect(Collectors.toSet()));

        articleService.saveArticle(articleInDTO);
    }

    @Test
    void testUpdateArticle() {
        String[] tags = {"标签2", "标签5", "标签6"};

        ArticleInDTO articleInDTO = new ArticleInDTO();

        articleInDTO.setId(24L);
        articleInDTO.setContent("文章内容");
        articleInDTO.setTitle("文章标题6");
        articleInDTO.setTags(Arrays.stream(tags).collect(Collectors.toSet()));

        articleService.updateArticle(articleInDTO);
    }

    @Test
    void generateArticle() {
        for (int i = 10000; i < 10200; i++) {
            Article article = new Article();
            article.setAbstractInfo("摘要~");
            article.setContent("内容~");
            article.setTitle("标题：" + i);
            article.setReading((long) (Math.random() * 10000));
            articleMapper.insertSelective(article);
        }
    }

    @Test
    void testGetHotArticles() {
        Response res = articleService.getHostArticles(10);
        System.out.println(res.getData());
    }

    @Test
    void testPageable() {
        Pageable pageable = new Pageable();
        pageable.setStartPage(3);
        pageable.setCount(10);
        pageable.getSorts().add(new Sort("reading", "asc"));

        Response res = articleService.getArticles(pageable);

        Object data = res.getData();
    }

    @Test
    void testDelete(){
        Response res = articleService.changeArticleStatus("100,952", 1003);
    }

    @Test
    void testSearch(){
        Pageable pageable = new Pageable();
        pageable.setStartPage(2);
        pageable.setCount(50);

        Response res = articleService.searchArticle(pageable,"标题");
        Object data = res.getData();
    }

    @Test
    void testGetDetail(){
        articleService.getDetailArticle(24L);
    }


    @Test
    void testArchive(){
        Pageable pageable = new Pageable();
        pageable.setCount(3);
        pageable.setStartPage(1);
        articleService.archiveArticles(pageable);
    }

    @Test
    void testGetArticleByTag(){
        // 53 tagid
        Pageable pageable = new Pageable();
        pageable.setCount(1);
        pageable.setStartPage(1);

        articleService.getTagServices(pageable,53L);
    }
}
