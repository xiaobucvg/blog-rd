package com.xiaobu.blog.util;

import com.xiaobu.blog.common.token.Token;
import com.xiaobu.blog.exception.ExpiresTokenException;
import com.xiaobu.blog.exception.IllegalTokenException;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Token 工具类
 *
 * @author zh  --2020/3/25 10:05
 */
@Slf4j
public class TokenUtil {

    private TokenUtil() {
    }

    private static long expTime = 1000 * 60 * 60;

    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    private static Mac hmacSHA256;

    static {
        try {
            hmacSHA256 = Mac.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("生成hmacSHA256实例失败.");
        }
    }

    // 签名密钥
    private static String key = "simple-blog";

    /**
     * 生成Token
     */
    public static String getToken() {
        Token token = new Token(expTime);

        String header = getTokenHeader(token);
        String body = getTokenBody(token);
        String headerAndBody = header + "." + body;

        String sign = getSign(headerAndBody);

        return headerAndBody + "." + sign;
    }

    public static String getToken(String sub) {
        Token token = new Token(sub,expTime);

        String header = getTokenHeader(token);
        String body = getTokenBody(token);
        String headerAndBody = header + "." + body;

        String sign = getSign(headerAndBody);

        return headerAndBody + "." + sign;
    }


    /**
     * 验证 token
     */
    public static void checkToken(String token) throws IllegalTokenException, ExpiresTokenException {
        String[] tokenArr = token.split("\\.");
        String header = tokenArr[0];
        String body = tokenArr[1];
        String sign = tokenArr[2];
        if (!Objects.equals(sign, getSign(header + "." + body))) {
            throw new IllegalTokenException("token 不合法");
        }
        String json = new String(decoder.decode(body.getBytes()));
        Long exp = Long.valueOf(Objects.requireNonNull(JsonUtil.stringToObject(json, Token.class)).getExp());
        long now = new Date().getTime();
        if (exp < now) {
            throw new ExpiresTokenException("token 已经过期");
        }
    }

    /**
     * 获取 Token 头
     */
    private static String getTokenHeader(Token token) {
        // 生成 token 头
        Map<String, String> tokenHeaderMap = new HashMap<>();
        tokenHeaderMap.put("typ", token.getTyp());
        tokenHeaderMap.put("alg", token.getAlg());
        byte[] tokenHeaderSrc = JsonUtil.objectToJsonBytes(tokenHeaderMap);
        return encoder.encodeToString(tokenHeaderSrc);
    }

    /**
     * 获取 Token 体
     */
    private static String getTokenBody(Token token) {
        Map<String, String> tokenBodyMap = new HashMap<>();
        tokenBodyMap.put("sub", token.getSub());
        tokenBodyMap.put("iss", token.getIss());
        tokenBodyMap.put("iat", token.getIat());
        tokenBodyMap.put("exp", token.getExp());
        tokenBodyMap.put("nbf", token.getNbf());
        tokenBodyMap.put("jti", token.getJti());
        byte[] tokenBodySrc = JsonUtil.objectToJsonBytes(tokenBodyMap);
        return encoder.encodeToString(tokenBodySrc);
    }

    /**
     * 生成签名
     */
    private static String getSign(String data) {
        try {
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
            hmacSHA256.init(secret_key);
            byte[] bytes = hmacSHA256.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte item : bytes) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 token 的 sub 信息
     */
    public static String getTokenSub(String token) {
        String[] tokenArr = token.split("\\.");
        String body = tokenArr[1];
        String json = new String(decoder.decode(body.getBytes()));
        return JsonUtil.stringToObject(json, Token.class).getSub();
    }
}

