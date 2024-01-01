package com.angelpro.controller;

import com.angelpro.service.MessageService;
import com.angelpro.vo.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
