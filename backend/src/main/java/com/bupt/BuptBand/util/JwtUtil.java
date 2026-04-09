package com.bupt.BuptBand.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil
{
    // 这是一个特殊的加密密钥，长度必须足够（32字节以上）
    // 正式环境会放在配置文件里，现在先写死一个
    private final String SECRET_KEY = "BuptBand_Secret_Key_For_RightSpell_2026";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 过期时间24h
    private final long EXPIRATION_TIME = 86400000;

    /**
     * 生成通行证（Token）
     */
    public String generateToken(String nickname)
    {
        return Jwts.builder()
                .subject(nickname) // 把昵称存进 Token
                .issuedAt(new Date()) // 签发时间
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(key) // 签名加密
                .compact();
    }

    /**
     * 从通行证里读出昵称
     */
    public String extractNickname(String token)
    {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}