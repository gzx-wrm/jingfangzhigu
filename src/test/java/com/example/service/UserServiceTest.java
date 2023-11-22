package com.example.service;

import com.example.exception.LoginException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author gzx
 * @date 2023/11/4
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Resource
    UserService userService;

    @Test
    public void testLogin() throws LoginException {
        String token = userService.LoginByPhonePassword("13888988898", "123456");
        System.out.println(token);
    }

}