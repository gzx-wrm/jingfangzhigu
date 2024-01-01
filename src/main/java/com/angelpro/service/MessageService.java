package com.angelpro.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.angelpro.mapper.MessageMapper;
import com.angelpro.pojo.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 消息服务
 */
@Service
public class MessageService {

    @Resource
    MessageMapper messageMapper;

    public Message getMessageById(String messageId) {
        return messageMapper.selectOne(Wrappers.<Message>query().eq("message_id", messageId));
    }

    @Transactional(propagation = Propagation.REQUIRED,  rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int createMessage(Message message) {
        return messageMapper.insert(message);
    }

    @Transactional(propagation = Propagation.REQUIRED,  rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int createMessage(String chatId, String messageId, String content, String role, Date date, String metadata) {
        Message message = new Message().setMessageId(messageId)
                .setChatId(chatId)
                .setContent(content)
                .setRole(role)
                .setCreateTime(date)
                .setMetadata(metadata)
                .setIsDelete(0);

        return messageMapper.insert(message);
    }

    @Transactional(propagation = Propagation.REQUIRED,  rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int deleteMessage(String chatId, String messageId) {
        return messageMapper.update(null, Wrappers.<Message>update().set("is_delete", 1).eq("message_id", messageId).eq("chat_id", chatId));
    }
}
