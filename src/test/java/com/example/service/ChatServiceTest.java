package com.example.service;

import com.example.exception.ChatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 测试对话服务
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChatServiceTest {

    @Resource
    ChatService chatService;

    @Resource
    MessageService messageService;

    @Resource
    JingFangService jingFangService;

    @Test
    public void test() {
        System.out.println(chatService.getChatsWithoutMessages("1"));
    }

    @Test
    public void test01() throws ChatException {
        System.out.println(chatService.getChatMessages("1", "1"));
    }
}