package com.example.config;

import com.example.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description mvc相关配置，比如拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/user/passwordLogin")
                .excludePathPatterns("/user/sendSms")
                .excludePathPatterns("/user/register")
                .excludePathPatterns("/user/smsLogin")
                .excludePathPatterns("/index")
                .excludePathPatterns("/guide")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/verifyCodeLogin")
                .excludePathPatterns("/static/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("static/assets/**")
                .addResourceLocations("classpath:/static/assets/");
    }
}
