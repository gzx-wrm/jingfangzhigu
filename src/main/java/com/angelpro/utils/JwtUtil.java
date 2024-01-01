package com.angelpro.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description
 */
public class JwtUtil {
    // 加密的密钥
    private static final String SECRET = "YOUR_JWT_SECRET";

    public static String generateToken(String userId) {
        return  Jwts.builder()
                .setSubject(userId)
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000)) // 过期时间为2小时
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public static String getTokenFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = "";
        for (Cookie cookie : cookies) {
            if ("Authorization".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        return parseToken(token);
    }

    public static String parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
