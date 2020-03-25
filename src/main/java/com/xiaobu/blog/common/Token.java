package com.xiaobu.blog.common;

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
    String sub = "admin";

    String iss = "http://101.201.122.174:8080/admin/token";

    String iat = String.valueOf(new Date().getTime());

    String exp;

    String nbf = String.valueOf(new Date().getTime());

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
