package com.example.utils;

import java.util.Random;

/**
 * @author gzx
 * @date 2023/11/5
 * @Description 生成一个随机6位验证码
 */
public class VerifyCodeUtil {

    private static final Random random = new Random();

    public static String getCode() {
        return String.valueOf(random.nextInt(900000) + 100000);
    }
}
