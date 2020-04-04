package com.xiaobu.blog.common.token;

import com.xiaobu.blog.util.NetUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

/**
 * 荷载
 *
 * @author zh  --2020/4/1 22:04
 */
@Data
@NoArgsConstructor
public class TokenPayload {
    // 表明获取该 token 的用户 ID
    String sub;
    // 签发机构
    String iss;
    // 签发时间
    String iat;
    // 在此时间之前不可使用
    String nbf = iat;
    // 过期时间
    String exp;
    // 随机字符串
    String jti;

    public TokenPayload(String sub, long expTime) {
        // 固定
        this.iss = NetUtil.getServerAddress();
        this.iat = String.valueOf(new Date().getTime());
        this.nbf = iat;
        this.jti = UUID.randomUUID().toString();
        // 计算出
        this.sub = sub;
        this.exp = String.valueOf(Long.parseLong(this.iat) + expTime);
    }
}
