package com.xiaobu.blog.common.token;

import com.xiaobu.blog.common.Const;
import lombok.Data;

/**
 * 头部
 * @author zh  --2020/4/1 22:04
 */
@Data
public class TokenHeader {
    String typ = Const.TOKEN_TYPE;
    String alg = Const.TOKEN_ALG;
}
