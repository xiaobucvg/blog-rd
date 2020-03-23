package com.xiaobu.blog.model.wrapper;

import com.xiaobu.blog.common.Convert;
import com.xiaobu.blog.dto.ArticleDetailOutDTO;
import com.xiaobu.blog.model.Article;
import com.xiaobu.blog.model.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zh  --2020/3/18 13:40
 */
@Data
@NoArgsConstructor
public class ArticleWithTag {
    private Article article;

    private Set<Tag> tags = new HashSet<>();

    public ArticleWithTag(Article article) {
        this.article = article;
    }

}
