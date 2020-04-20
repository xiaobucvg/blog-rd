package com.xiaobu.blog.aspect.util;

import com.xiaobu.blog.common.page.Pageable;

/**
 * 分页对象后续处理器
 *
 * @author zh  --2020/4/19 21:25
 */
public class PageableAfterProcessor implements AfterProcessor<Pageable> {
    @Override
    public Object process(Pageable pageable) {
        pageable.calculate();
        return null;
    }
}
