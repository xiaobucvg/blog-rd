package com.xiaobu.blog.common.page;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 分页包装对象
 *
 * @author zh  --2020/3/18 16:30
 */
@Data
public class Page {
    private int pageCount;      // 总共有多少页

    private int currentPage;    // 当前页码

    private boolean hasPrePage; // 是否有前一页

    private boolean hasNextPage; // 是否有后一页

    private int count;          // 获取到的数量

    private Object list;        // 具体的内容

    private Page() {

    }

    /**
     * 创建分页
     *
     * @param pageable   分页查询信息
     * @param counts     查询的内容的总长度
     * @param collection 查询后的结果
     */
    public static Page createPage(Pageable pageable, long counts, Object collection) {
        Page page = new Page();
        // 计算总页数
        int pageCount = (int) counts / pageable.getCount();
        if(counts % pageable.getCount() != 0){
            pageCount ++;
        }
        page.setPageCount(pageCount);
        // 当前页码
        page.setCurrentPage(pageable.getStartPage());
        // 获取到的内容数量
        if(collection instanceof Map){
            page.setCount(((Map) collection).keySet().size());
        }
        else if(collection instanceof List){
            page.setCount(((List) collection).size());
        }
        // 是否有前一页
        if (pageable.getStartPage() > 1) {
            page.setHasPrePage(true);
        }
        // 是否有后一页
        if (pageable.getStartPage() < page.getPageCount()) {
            page.setHasNextPage(true);
        }
        // 内容
        page.setList(collection);
        return page;
    }
}
