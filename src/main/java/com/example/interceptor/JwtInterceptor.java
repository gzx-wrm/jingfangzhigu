package com.example.interceptor;

import com.example.utils.JwtUtil;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 拦截器用于验证JWT认证信息
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 验证jwt信息
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            // 直接重定向到登录页面
            response.sendRedirect("/index");
            return false;
        }

        for (Cookie cookie : cookies) {
            if ("Authorization".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) {
            response.sendRedirect("/index");
            return false;
        }

        try {
            // 验证JWT
            String userId = JwtUtil.parseToken(token);
            return true;
        } catch (JwtException ex) {
            response.sendRedirect("/index");
            return false;
        }
    }
}
