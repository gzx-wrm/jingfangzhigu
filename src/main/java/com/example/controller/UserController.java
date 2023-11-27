package com.example.controller;

import com.example.exception.FeedbackException;
import com.example.exception.LoginException;
import com.example.exception.RegisterException;
import com.example.service.FeedbackService;
import com.example.service.UserService;
import com.example.utils.JwtUtil;
import com.example.utils.VerifyCodeUtil;
import com.example.pojo.Feedback;
import com.example.vo.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 用户相关api
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    FeedbackService feedbackService;

    /**
     * 用户根据手机号、验证码、密码注册
     *
     * @param phone       手机号
     * @param code        验证
     * @param encPassword 加密后的密码
     * @param request     请求
     * @param response    响应
     * @return 注册结果
     * @throws RegisterException 注册异常
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestParam(name = "phone") String phone,
                           @RequestParam(name = "code") String code,
                           @RequestParam(name = "pwd") String encPassword,
                           HttpServletRequest request,
                           HttpServletResponse response) throws RegisterException {
        String token = userService.register(phone, code, (String) request.getSession().getAttribute("code"), encPassword);

        // 将登陆成功的token写到cookie中
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge(60 * 60 * 2);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 将session中的验证码失效
        request.getSession().removeAttribute("code");

        return Result.ok("注册成功！").data("redirectUrl", "/main");
    }

    /**
     * 账号密码登陆
     *
     * @param accountName 账号，可以是手机号，也可以是邮箱
     * @param encPassword 密码，md5加密后
     *                    在响应头中加上jwt验证信息，返回登录成功标识
     */
    @RequestMapping(value = "/passwordLogin", method = RequestMethod.POST)
    public Result loginByPhonePassword(@RequestParam(name = "account") String accountName,
                                       @RequestParam(name = "pwd") String encPassword,
                                       HttpServletResponse response) throws LoginException {
        String token = userService.LoginByPhonePassword(accountName, encPassword);

        // 将登陆成功的token写到cookie中
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge(60 * 60 * 2);
        cookie.setPath("/");
        response.addCookie(cookie);

        return Result.ok("登录成功！").data("redirectUrl", "/main");
    }

    /**
     * 发送验证码，并将正确的验证码保存在session中
     *
     * @param phone 手机号码
     * @return 是否发送成功标识
     */
    @RequestMapping("/sendSms")
    public Result sendSms(@RequestParam(name = "phone") String phone,
                          HttpServletRequest request) {
        // 生成一个随机验证码
        String code = VerifyCodeUtil.getCode();
        request.getSession().setAttribute("code", "123456");
        return Result.ok("验证码发送成功！");


        // 发送验证码并设置验证码有效时间
//        if (userService.sendPhoneSms(phone, code)){
//            request.getSession().setAttribute("code", code);
//            // 设置有效时间为5分钟
//            request.getSession().setMaxInactiveInterval(5);
//            return Result.ok("验证码发送成功！");
//        }
//        else
//            return Result.error("验证码发送失败！");
    }

    /**
     * 短信验证码登录，先判断账号是否存在，若存在则检查session中的短信是否与code相同
     *
     * @param phone    手机号
     * @param code     验证码
     * @param request  请求对象
     * @param response 响应对象
     * @return 登录是否成功标识
     */
    @RequestMapping(value = "/smsLogin", method = RequestMethod.POST)
    public Result loginByPhoneSms(@RequestParam(name = "phone") String phone,
                                  @RequestParam(name = "code") String code,
                                  HttpServletRequest request,
                                  HttpServletResponse response) throws LoginException {
        String token = userService.LoginByPhoneSms(phone, (String) request.getSession().getAttribute("code"), code);

        // 将登陆成功的token写到cookie中
        Cookie cookie = new Cookie("Authorization", token);
        cookie.setMaxAge(60 * 60 * 2);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 将session中的验证码失效
        request.getSession().removeAttribute("code");

        return Result.ok("登录成功！").data("redirectUrl", "/main");
    }

    /**
     * 用户退出登录
     *
     * @param request  请求
     * @param response 响应
     * @return 登出结果
     */
    // TODO 该登出接口是简化版的实现，只将客户端上的token擦去，但是实际上若用户保存了token还是可以验证通过(设置了token的有效期是2小时，只能等有效期到，否则token无法手动注销，因为jwt是无状态的)
    // TODO 实现完整注销的操作是将注销地jwt加入到黑名单中，只有不在黑名单中的token才能有效，可以使用redis实现
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return Result.ok("退出成功！");
    }


    /**
     * 用户上传反馈信息
     *
     * @param feedback 反馈内容，包括主题和内容
     * @param imgs     附带的图片
     * @return 反馈结果
     */
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public Result feedback(Feedback feedback,
                           @RequestParam(name = "imgs") MultipartFile[] imgs,
                           HttpServletRequest request) throws FeedbackException, IOException {
        // 获取用户id
        String userId = JwtUtil.getTokenFromRequest(request);
        feedback.setUserId(userId);
        // 上传反馈信息，获取反馈信息的id，以此id来创建路径存储图像
        int feedbackId = feedbackService.commitFeedbackText(feedback);

        for (MultipartFile file : imgs) {
            feedbackService.store(file, feedbackId);
        }

        return Result.ok("反馈成功！");
    }
}