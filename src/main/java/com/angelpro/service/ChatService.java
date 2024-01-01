package com.angelpro.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.angelpro.exception.ChatException;
import com.angelpro.mapper.ChatMapper;
import com.angelpro.mapper.MessageMapper;
import com.angelpro.pojo.Chat;
import com.angelpro.pojo.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 用户chat服务
 */
@Service
public class ChatService {

    @Resource
    ChatMapper chatMapper;

    @Resource
    MessageMapper messageMapper;

    public ArrayList<Chat> getChatsWithoutMessages(String userId) {
        // 根据用户id查找chats
        List<Chat> chats = chatMapper.selectList(Wrappers.<Chat>query().eq("user_id", userId).eq("is_delete", 0).orderByAsc("create_time"));
        return (ArrayList<Chat>) chats;
    }

    public Chat getChatMessages(String userId, String chatId) throws ChatException {
        Chat chat = chatMapper.selectOne(Wrappers.<Chat>query().eq("user_id", userId).eq("chat_id", chatId).eq("is_delete", 0));

        if (chat == null)
            throw new ChatException("对话不存在！");

        List<Message> messages = messageMapper.selectList(Wrappers.<Message>query().eq("chat_id", chatId).eq("is_delete", 0).orderByAsc("create_time"));
        chat.setMessage((ArrayList<Message>) messages);

        return chat;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int createChat(String userId, Chat chat) {
        Date date = new Date();
        chat.setUserId(userId)
                .setCreateTime(date)
                .setUpdateTime(date)
                .setIsDelete(0);

        return chatMapper.insert(chat);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    public int deleteChat(String userId, String chatId) {
        int i = chatMapper.update(null, Wrappers.<Chat>update().set("is_delete", 1).eq("user_id", userId).eq("chat_id", chatId));
        // 删除有关的消息
        messageMapper.update(null, Wrappers.<Message>update().set("is_delete", 1).eq("chat_id", chatId));
        return i;
    }
}
