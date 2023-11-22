package com.example.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageServiceTest {

    @Resource
    MessageService messageService;

    @Test
    public void test() {
        System.out.println(messageService.getMessageById("1"));
    }

    @Test
    public void test01() {
        messageService.deleteMessage("1", "3cf51193-8b64-4580-a1ef-f5126f9e0ff2");
    }
}