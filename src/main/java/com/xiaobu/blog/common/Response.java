package com.xiaobu.blog.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zh  --2020/3/17 20:31
 */
@Data
public class Response {

    private int code;

    private String msg;

    private Object data;

    @JsonIgnore
    private boolean success;

    private Response(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    // 创建实例
    private static Response newInstance(boolean success, int code, String msg, Object data) {
        Response res = new Response(code, msg, data);
        res.setSuccess(success);
        return res;
    }

    // 创建响应成功的实例
    private static Response newSuccessInstance(int code, String msg, Object data) {
        return newInstance(true, code, msg, data);
    }

    public static Response newSuccessInstance(String msg) {
        return newSuccessInstance(HttpServletResponse.SC_OK, msg, null);
    }

    public static Response newSuccessInstance(String msg, Object data) {
        return newSuccessInstance(HttpServletResponse.SC_OK, msg, data);
    }

    // 创建响应失败的实例
    public static Response newFailInstance(int code, String msg, Object data) {
        return newInstance(false, code, msg, data);
    }
}
