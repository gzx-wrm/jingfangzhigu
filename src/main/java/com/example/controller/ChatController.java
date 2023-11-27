package com.example.controller;

import com.example.exception.ChatException;
import com.example.pojo.Chat;
import com.example.service.ChatService;
import com.example.utils.JwtUtil;
import com.example.vo.Result;
import io.jsonwebtoken.Jwt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
     * @return 查询结果
     */
    @RequestMapping("/")
    public Result getAllChats(HttpServletRequest request) {
        String userId = JwtUtil.getTokenFromRequest(request);
        ArrayList<Chat> chats = chatService.getChatsWithoutMessages(userId);

        return Result.ok().data("chats", chats);
    }

    /**
     * 获取某个chat的所有messages
     * @param chatId 对话id
     * @return 查询结果
     * @throws ChatException 查询异常
     */
    @RequestMapping("/{chatId}")
    public Result getChatMessages(@PathVariable String chatId,
                                  HttpServletRequest request) throws ChatException {
        String userId = JwtUtil.getTokenFromRequest(request);
        Chat chat = chatService.getChatMessages(userId, chatId);

        return Result.ok().data("chat", chat);
    }

    /**
     * 用户新建一个会话
     * @param chat 对chat进行初步的封装
     * @return 是否成功的标志
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Result createChat(@RequestBody Chat chat,
                             HttpServletRequest request) {
        String userId = JwtUtil.getTokenFromRequest(request);
        if (chatService.createChat(userId, chat) > 0)
            return Result.ok("新建会话成功！");
        else
            return Result.error("新建会话失败！");
    }

    /**
     * 删除用户的一个chat
     * @param chatId chatId
     * @return 是否删除成功
     */
    @RequestMapping(value = "/{chatId}", method = RequestMethod.DELETE)
    public Result deleteChat(@PathVariable String chatId,
                             HttpServletRequest request) {
        String userId = JwtUtil.getTokenFromRequest(request);
        if (chatService.deleteChat(userId, chatId) > 0)
            return Result.ok("删除成功！");
        else
            return Result.error("删除失败！");
    }
}
