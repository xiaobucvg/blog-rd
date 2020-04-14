package com.xiaobu.blog.common.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zh  --2020/3/25 14:39
 */
@Data
@NoArgsConstructor
public class Token {

    private TokenHeader tokenHeader;

    private TokenPayload tokenPayload;

    private String sign;


    public Token(String sub, Long tokenExpTime) throws JsonProcessingException {
        this.tokenHeader = new TokenHeader();
        this.tokenPayload = new TokenPayload(sub, tokenExpTime);
    }
}
