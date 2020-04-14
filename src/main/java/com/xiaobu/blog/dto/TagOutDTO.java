package com.xiaobu.blog.dto;

import com.xiaobu.blog.dto.interfaces.Convert;
import com.xiaobu.blog.model.Tag;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * @author zh  --2020/3/21 9:32
 */
@Data
public class TagOutDTO implements Convert<Tag> {

    private Long id;

    private String name;


    @Override
    public TagOutDTO copyFrom(Tag tag) {
        BeanUtils.copyProperties(tag,this);
        return this;
    }
}
