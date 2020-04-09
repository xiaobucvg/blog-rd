package com.xiaobu.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaobu.blog.model.wrapper.ArticleWithTag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zh  --2020/3/21 9:23
 */
@Data
public class ArticleDetailOutDTO implements Convert<ArticleWithTag> {
    private Long id;

    private int status;

    private String title;

    private String abstractInfo;

    private String content;

    private Long reading;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Set<TagOutDTO> tags;

    @Override
    public ArticleDetailOutDTO copyFrom(ArticleWithTag articleWithTag) {
        BeanUtils.copyProperties(articleWithTag.getArticle(),this);

        HashSet<TagOutDTO> dtos = new HashSet<>();

        articleWithTag.getTags().forEach(tag -> {
            TagOutDTO dto = new TagOutDTO();
            BeanUtils.copyProperties(tag, dto);
            dtos.add(dto);
        });
        this.tags = dtos;
        return this;
    }
}
