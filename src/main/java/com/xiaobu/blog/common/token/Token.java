package com.xiaobu.blog.common.token;

import com.xiaobu.blog.util.InetUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * @author zh  --2020/3/25 14:39
 */
@Data
@NoArgsConstructor
public class Token {

    // token 头
    String typ = "jwt";

    String alg = "HmacSHA256";

    // token 体
    // 表明该 token 面向的用户
    String sub = "";
    // 签发机构
    String iss = "http://" + InetUtil.getLocalHost() + ":8080/admin/token";
    // 签发时间
    String iat = String.valueOf(new Date().getTime());
    // 过期时间
    String exp;
    // 在此时间之前不可使用
    String nbf = String.valueOf(new Date().getTime());
    // 随机字符串
    String jti = UUID.randomUUID().toString();

    public Token(long time) {
        Long iat = Long.valueOf(this.iat);
        Long exp = iat + time;
        this.exp = String.valueOf(exp);
    }

    public Token(String sub,long time) {
        this(time);
        this.sub = sub;
    }
}
