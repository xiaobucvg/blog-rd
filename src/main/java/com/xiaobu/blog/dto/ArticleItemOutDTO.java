package com.xiaobu.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobu.blog.common.Convert;
import com.xiaobu.blog.model.Article;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * - 博文管理
 * - 图表
 * @author zh  --2020/3/18 15:33
 */
@Data
public class ArticleItemOutDTO implements Convert<Article> {
    private Long id;

    private String title;

    private Integer status;

    private String abstractInfo;

    private Long reading;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private Date updateTime;

    @Override
    public ArticleItemOutDTO toModel(Article article) {
        ArticleItemOutDTO res = new ArticleItemOutDTO();
        BeanUtils.copyProperties(article,res);
        return res;
    }
}
