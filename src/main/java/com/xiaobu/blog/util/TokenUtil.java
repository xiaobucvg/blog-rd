package com.xiaobu.blog.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xiaobu.blog.common.Const;
import com.xiaobu.blog.common.token.Token;
import com.xiaobu.blog.common.token.TokenHeader;
import com.xiaobu.blog.common.token.TokenPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

/**
 * Token 工具类
 *
 * @author zh  --2020/3/25 10:05
 */
@Slf4j
@Component
public class TokenUtil {

    @Autowired
    private JSONUtil jsonUtil;

    // token 缓存区
    private ExpiryMap<String, Token> tokenMap = new ExpiryMap<>();
    // 编码器
    private Base64.Encoder encoder = Base64.getEncoder();
    // 解码器
    private Base64.Decoder decoder = Base64.getDecoder();
    // 加密器
    private Mac hmacSHA256;
    // 密钥
    @Value("custom.jwt.secret")
    private String key = "default";


    public TokenUtil() {
        try {
            hmacSHA256 = Mac.getInstance(Const.TOKEN_ALG);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("生成{}实例失败", Const.TOKEN_ALG);
            throw new Error("系统启动失败");
        }
    }

    /**
     * 生成Token
     * 放入缓存
     *
     * @param sub token 持有者
     */
    public String getToken(String sub) throws Exception {
        Token token = new Token(sub, Const.TOKEN_EXP_TIME);
        String header = this.getTokenHeader(token);
        String payload = this.getTokenPayload(token);
        String sign = this.getSign(header + "." + payload);
        token.setSign(sign);
        String tokenStr = header + "." + payload + "." + sign;
        this.tokenMap.put(tokenStr, token, Const.TOKEN_EXP_TIME);
        return tokenStr;
    }

    private String getTokenHeader(Token token) throws JsonProcessingException {
        TokenHeader tokenHeader = token.getTokenHeader();
        byte[] tokenHeaderSrc = jsonUtil.objectToJsonBytes(tokenHeader);
        return encoder.encodeToString(tokenHeaderSrc);
    }

    private String getTokenPayload(Token token) throws JsonProcessingException {
        TokenPayload tokenPayload = token.getTokenPayload();
        byte[] tokenBodySrc = jsonUtil.objectToJsonBytes(tokenPayload);
        return encoder.encodeToString(tokenBodySrc);
    }

    private String getSign(String data) throws InvalidKeyException {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), Const.TOKEN_ALG);
        hmacSHA256.init(secretKey);
        byte[] bytes = hmacSHA256.doFinal(data.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte item : bytes) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 反向解析 token
     */
    public Token parseToken(String token) throws JsonProcessingException {
        String[] strs = token.split("\\.");
        TokenHeader header = jsonUtil.stringToObject(new String(decoder.decode(strs[0])), TokenHeader.class);
        TokenPayload payload = jsonUtil.stringToObject(new String(decoder.decode(strs[1])), TokenPayload.class);
        Token res = new Token();
        res.setTokenHeader(header);
        res.setTokenPayload(payload);
        res.setSign(strs[2]);
        return res;
    }

    /**
     * 验证 token 是否有效
     */
    public boolean isValidationToken(String tokenStr) {
        // 1. 查询缓存，如果存在，则绕过过期检查，并且刷新 token 在缓存中的有效期
        Token token = this.tokenMap.get(tokenStr);
        if (token != null) {
            this.tokenMap.put(tokenStr, token, Const.TOKEN_EXP_TIME);
            return true;
        }
        // 2. 缓存中不存在，验证 token 是否合法
        try {
            // 验证 token 是否是伪造或被修改过
            token = this.parseToken(tokenStr);
            String header = this.getTokenHeader(token);
            String payload = this.getTokenPayload(token);
            String sign = this.getSign(header + "." + payload);
            if (!Objects.equals(sign, token.getSign())) {
                return false;
            }
            // 验证 token 是否过期
            long endTime = Long.parseLong(token.getTokenPayload().getExp());
            long startTime = Long.parseLong(token.getTokenPayload().getNbf());
            long currentTime = System.currentTimeMillis();
            return currentTime >= startTime && currentTime < endTime;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取 sub 字段，在这里等于获取的用户 ID
     */
    public Long getTokenSub(String token) {
        try {
            return Long.valueOf(this.parseToken(token).getTokenPayload().getSub());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("获取 token sub 字段失败");
        }
    }
}

