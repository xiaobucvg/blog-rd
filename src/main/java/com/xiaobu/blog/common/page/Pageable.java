package com.xiaobu.blog.common.page;

import lombok.Data;

import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

/**
 * 分页查询对象
 *
 * @author zh  --2020/3/18 16:27
 */
@Data
public class Pageable {
    @Min(value = 1,message = "最小的开始页码为 1")
    private int startPage; // 开始页码,从 1 开始

    @Min(value = 1,message = "每页的数量最少为 1")
    private int count; // 每页数量

    private Set<Sort> sorts = new HashSet<>(); // 需要排序的字段和排序规则

    private int start;     // 开始位置

    private int end;       // 结束位置

    /** 计算开始位置和结束位置 */
    public void calculate(){
         int start = (this.startPage - 1) * this.count;
        this.setStart(start);
        int end = this.count;
        this.setEnd(end);
    }
}


