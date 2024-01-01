package com.angelpro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gzx
 * @date 2023/11/15
 * @Description 控制页面跳转
 */
@Controller
public class RouteController {

    @RequestMapping("/")
    public String defaultPage() {
        return "guide";
    }

    @RequestMapping("/guide")
    public String guide() {
        return "guide";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/register")
    public String goRegister() {
        return "register";
    }

    @RequestMapping("/login")
    public String goLogin() {
        return "login";
    }

    @RequestMapping("/verifyCodeLogin")
    public String goVerifyCodeLogin() {
        return "verifycode_login";
    }

    @RequestMapping("/test")
    public String test() {
        return "test";
    }

    @RequestMapping("/main")
    public String goChat() {
        return "chat2";
    }
}
