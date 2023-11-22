package com.example.controller;

import com.example.exception.ChatException;
import com.example.pojo.Chat;
import com.example.service.ChatService;
import com.example.vo.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 用户的工作区服务
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Resource
    ChatService chatService;

    /**
     * 根据用户id查询所有对话
     * @param userId 用户id
     * @return 查询结果
     */
    @RequestMapping("/{userId}")
    public Result getAllChats(@PathVariable String userId) {
        ArrayList<Chat> chats = chatService.getChatsWithoutMessages(userId);

        return Result.ok().data("chats", chats);
    }

    /**
     * 获取某个chat的所有messages
     * @param userId 用户id
     * @param chatId 对话id
     * @return 查询结果
     * @throws ChatException 查询异常
     */
    @RequestMapping("/{userId}/{chatId}")
    public Result getChatMessages(@PathVariable String userId,
                                  @PathVariable String chatId) throws ChatException {
        Chat chat = chatService.getChatMessages(userId, chatId);

        return Result.ok().data("chat", chat);
    }

    /**
     * 用户新建一个会话
     * @param userId 用户id
     * @param chatId 会话id 前端生成随机的uuid
     * @return 是否成功的标志
     */
    @RequestMapping(value = "/{userId}/{chatId}", method = RequestMethod.POST)
    public Result createChat(@PathVariable String userId,
                             @PathVariable String chatId) {
        if (chatService.createChat(userId, chatId) > 0)
            return Result.ok("新建会话成功！");
        else
            return Result.error("新建会话失败！");
    }

    /**
     * 删除用户的一个chat
     * @param userId 用户id
     * @param chatId chatId
     * @return
     */
    @RequestMapping(value = "/{userId}/{chatId}", method = RequestMethod.DELETE)
    public Result deleteChat(@PathVariable String userId,
                             @PathVariable String chatId) {
        if (chatService.deleteChat(userId, chatId) > 0)
            return Result.ok("删除成功！");
        else
            return Result.error("删除失败！");
    }
}
