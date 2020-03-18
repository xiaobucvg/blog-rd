package com.xiaobu.blog.common;

/**
 * 常量类
 *
 * @author zh  --2020/3/17 21:15
 */
public class Const {


    // 文章状态
    public enum ArticleStatus{
        PUBLISHED(1001,"已发布"),
        NOT_PUBLISHED(1002,"未发布"),
        DELETED(1003,"已删除"),
        TOP(1004,"置顶中"),
        DRAFT(1005,"草稿");

        int code;
        String msg;

        ArticleStatus(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


    // 排序规则
    public enum SORT{
        ESC("esc"),
        DESC("desc");

        private String sort;

        SORT(String sort) {
            this.sort = sort;
        }
    }

}
