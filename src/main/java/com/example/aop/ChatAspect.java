package com.example.aop;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.mapper.ChatMapper;
import com.example.pojo.Chat;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author gzx
 * @date 2023/11/18
 * @Description 用于在更新chat的update_time
 */
@Aspect
@Component
public class ChatAspect {

    @Resource
    ChatMapper chatMapper;

    // 在创建和删除message时更新chat update_time
    @Pointcut("execution(int com.example.service.MessageService.*(..))")
    private void updateChatTimePointCut(){}

    @Transactional(propagation = Propagation.REQUIRED,  rollbackFor = Exception.class, isolation = Isolation.DEFAULT)
    @AfterReturning(value = "updateChatTimePointCut()", returning = "result")
    public int updateChatTime(JoinPoint joinPoint, Object result) {
        if ((int) result >= 1) {
            String chatId = (String) joinPoint.getArgs()[0];
            return chatMapper.update(null, Wrappers.<Chat>update().set("update_time", new Date()).eq("chat_id", chatId));
        }
        return 0;
    }
}
