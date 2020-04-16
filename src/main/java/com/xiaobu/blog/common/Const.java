package com.xiaobu.blog.common;

/**
 * 常量类
 *
 * @author zh  --2020/3/17 21:15
 */
public class Const {

    // token 的类型
    public static final String TOKEN_TYPE = "jwt";

    // token 加密方式
    public static final String TOKEN_ALG = "HmacSHA256";

    // token 过期时间
    public static final long TOKEN_EXP_TIME = (long) (1000 * 60 * 60);

    // 请求头 token 对应的 key
    public static final String TOKEN = "auth-token";

    // 上传图片最大限制 byte
    public static final long FILE_IMAGE_MAX_SIZE = (long) (8 * 1024 * 1024 * 10);

    // 文章状态
    public enum ArticleStatus {
        PUBLISHED(1001, "已发布"),
        NOT_PUBLISHED(1002, "未发布"),
        DELETED(1003, "已删除"),
        TOP(1004, "置顶中"),
        ABOUT(1005, "关于我"),
        LINK(1006, "友情链接");

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

}
