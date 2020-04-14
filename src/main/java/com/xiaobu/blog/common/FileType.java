package com.xiaobu.blog.common;

/**
 * 文件类型
 *
 * @author zh  --2020/4/14 10:37
 */
public enum FileType {
    ALL(new String[]{}),  // 所有类型
    IMAGE(Const.IMAGE_MIMES);// 图片类型

    private String[] mime;

    FileType(String[] mime) {
        this.mime = mime;
    }

    public String[] getMime() {
        return mime;
    }
}
