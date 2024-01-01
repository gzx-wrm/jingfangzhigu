package com.angelpro.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.angelpro.exception.LoginException;
import com.angelpro.exception.RegisterException;
import com.angelpro.mapper.UserMapper;
import com.angelpro.pojo.User;
import com.angelpro.utils.JwtUtil;
import com.angelpro.utils.SmsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description 用户相关服务逻辑
 */
@Service
@Slf4j
public class UserService {

    @Resource
    UserMapper userMapper;

    @Resource
    SmsUtil smsUtil;

    /**
     * 用户通过手机号、验证码、密码进行注册
     * @param accountName 手机号
     * @param rightCode 正确的验证码
     * @param code 用户提供的验证码
     * @param encPassword 加密后的密码
     * @return 若注册成功返回token
     * @throws RegisterException 注册相关异常
     */
    @Transactional(propagation = Propagation.SUPPORTS,  rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public String register(String accountName, String code, String rightCode, String encPassword) throws RegisterException {
        // 先检查用户是否存在
        User user = userMapper.selectOne(Wrappers.<User>query().eq("phone", accountName));
        if (user != null)
            throw new RegisterException("账号已存在！");

        if (rightCode == null || "".equals(rightCode))
            throw new RegisterException("请发送验证码！");

        // 然后在判断验证码是否相等
        if (!rightCode.equals(code))
            throw new RegisterException("验证码错误！");
        else {
            User user1 = new User();
            user1.setUserId(UUID.randomUUID().toString())
                    .setPassword(encPassword)
                    .setPhone(accountName);
            if (userMapper.insert(user1) == 1) {
                log.debug("用户" + user1.getUserId() + "注册");
                return JwtUtil.generateToken(user1.getUserId());
            }
        }
        throw new RegisterException("未知的错误，请重试！");
    }

    /**
     * 手机号密码登录
     * @param accountName 账号
     * @param encPassword 加密后的密码
     * @return token
     */
    public String LoginByPhonePassword(String accountName, String encPassword) throws LoginException {
        User user = userMapper.selectOne(Wrappers.<User>query().eq("phone", accountName));
        if (user == null)
            throw new LoginException("账号不存在!");
        else if (!user.getPassword().equals(encPassword))
            throw new LoginException("密码错误!");
        else {
            log.debug("用户" + user.getUserId() + "登录");
            return JwtUtil.generateToken(user.getUserId());
        }
    }

    /**
     * 手机验证码登录
     * @param accountName 手机号
     * @param rightCode 正确的验证码
     * @param code 用户提供的验证码
     * @return token
     * @throws LoginException 登录失败异常
     */
    public String LoginByPhoneSms(String accountName, String rightCode, String code) throws LoginException {
        User user = userMapper.selectOne(Wrappers.<User>query().eq("phone", accountName));
        if (user == null)
            throw new LoginException("账号不存在!");
        else if (rightCode == null || "".equals(rightCode))
            throw new LoginException("请发送验证码！");
        else if (!rightCode.equals(code))
            throw new LoginException("验证码错误！");
        else {
            log.debug("用户" + user.getUserId() + "登录");
            return JwtUtil.generateToken(user.getUserId());
        }
    }

    /**
     * 发送验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 是否发送成功
     */
    public boolean sendPhoneSms(String phone, String code) {
        return smsUtil.sendSms(phone, code);
    }
}
