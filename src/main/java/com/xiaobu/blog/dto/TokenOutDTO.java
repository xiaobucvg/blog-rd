package com.xiaobu.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token 传输对象
 *
 * @author zh  --2020/4/4 9:06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenOutDTO {
    private String token;

    // 有效时间
    private Long expiresTime;
}
