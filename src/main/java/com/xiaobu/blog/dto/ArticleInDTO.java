package com.xiaobu.blog.dto;

import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.model.Article;
import com.xiaobu.blog.model.Tag;
import com.xiaobu.blog.model.wrapper.ArticleWithTag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * - 发布文章
 * - 更新文章
 *
 * @author zh  --2020/3/17 20:42
 */
@Data
public class ArticleInDTO implements Convert<ArticleWithTag> {

    private Long id;

    @NotBlank(message = "文章标题不能为空")
    private String title;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    @NotEmpty(message = "文章至少要有一个标签")
    private Set<String> tags = new HashSet<>();

    private Integer status = Const.ArticleStatus.PUBLISHED.getCode();

    @Override
    public ArticleWithTag toModel() {
        Article article = new Article();
        BeanUtils.copyProperties(this, article);
        article.setAbstractInfo(extractAbstractInfo(this.content));
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setReading(0L);

        ArticleWithTag res = new ArticleWithTag(article);

        tags.forEach(tagName -> {
            Tag tag = new Tag();
            tag.setCreateTime(new Date());
            tag.setUpdateTime(new Date());
            tag.setName(tagName);
            res.getTags().add(tag);
        });

        return res;
    }

    private String extractAbstractInfo(String content) {

        String pattern = "<\\/?.+?>";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        String res = m.replaceAll("");

        if (res.length() > 255) {
            return res.substring(0, 255);
        }
        return res;
    }
}
