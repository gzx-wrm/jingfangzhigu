package com.example.controller;

import com.example.service.MessageService;
import com.example.vo.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 消息控制器
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    @Resource
    MessageService messageService;

    @RequestMapping(value = "{chatId}/{messageId}", method = RequestMethod.DELETE)
    public Result deleteMessage(@PathVariable String chatId,
                                @PathVariable String messageId) {
        if(messageService.deleteMessage(chatId, messageId) > 0)
            return Result.ok("删除成功！");
        else
            return Result.error("删除失败！");
    }
}
