package com.xiaobu.blog.mapper;

import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.model.Article;
import com.xiaobu.blog.model.ArticleExample;
import com.xiaobu.blog.model.wrapper.ArticleWithTag;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ArticleMapper {
    int _insertArticleSelective(Article article);

    int _insertArticleTag(ArticleWithTag articleWithTag);

    int _deleteAllTags(Article article);

    List<Article> _selectHotArticles(int count);

    List<Article> _selectArticles(Pageable pageable);

    int _updateArticleStatusByIds(@Param("id_list") List<String> idList, @Param("status") Integer status);

    List<Article> _selectByKeywords(@Param("pageable") Pageable pageable, @Param("keywords") String keywords);

    long _countArticlesByKeywords(String keywords);

    ArticleWithTag _selectArticleWithTag(Long id);

    List<Article> _selectPublishedArticlesByPage(Pageable pageable);

    long _countPublishedArticlesByKeywords(String keywords);

    List<Article> _selectPublishedArticlesByKeywords(Pageable pageable, String keywords);

    List<String> _selectMonth(Pageable pageable);

    List<Article> _selectArticlesByMonth(String month);

    long _selectArchiveCounts();

    List<Article> _selectPublishedArticlesByTag(@Param("tagid") Long tagid, @Param("pageable") Pageable pageable);

    int _addReading(long id);

    List<Article> _selectDeletedArticles(Pageable pageable);

    int _deleteTagsByArticles(List<Article> articles);

    ArticleWithTag _forceSelectArticleWithTag(long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    long countByExample(ArticleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int deleteByExample(ArticleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int insert(Article record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int insertSelective(Article record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    List<Article> selectByExampleWithBLOBs(ArticleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    List<Article> selectByExample(ArticleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    Article selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int updateByExampleSelective(@Param("record") Article record, @Param("example") ArticleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int updateByExampleWithBLOBs(@Param("record") Article record, @Param("example") ArticleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int updateByExample(@Param("record") Article record, @Param("example") ArticleExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int updateByPrimaryKeySelective(Article record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int updateByPrimaryKeyWithBLOBs(Article record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sm_article
     *
     * @mbg.generated Wed Mar 18 13:37:33 CST 2020
     */
    int updateByPrimaryKey(Article record);

}