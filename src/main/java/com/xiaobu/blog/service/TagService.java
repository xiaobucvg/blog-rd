package com.xiaobu.blog.service;

import com.xiaobu.blog.common.response.Response;
import com.xiaobu.blog.common.page.Page;
import com.xiaobu.blog.common.page.Pageable;
import com.xiaobu.blog.dto.TagOutDTO;
import com.xiaobu.blog.mapper.TagMapper;
import com.xiaobu.blog.model.Tag;
import com.xiaobu.blog.model.TagExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zh  --2020/3/22 16:47
 */
@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;

    public Response getTags(Pageable pageable) {
        pageable.calculate();

        // 1. 查询数量
        long count = tagMapper.countByExample(new TagExample());

        // 2. 查询具体数据
        List<Tag> tags = tagMapper._selectTagsByPageable(pageable);
        List<TagOutDTO> tagOutDTOS = convertArticleBatch(tags);

        // 3. 封装返回
        Page page = Page.createPage(pageable, count, tagOutDTOS);

        return Response.newSuccessInstance("获取标签记录成功",page);

    }

    /**
     * 批量转换 Tag
     */
    private List<TagOutDTO> convertArticleBatch(List<Tag> tags) {
        List<TagOutDTO> newTagDTOList = new ArrayList<>();

        tags.forEach(tag -> {
            newTagDTOList.add(new TagOutDTO().toModel(tag));
        });

        return newTagDTOList;
    }
}
