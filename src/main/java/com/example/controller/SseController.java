package com.example.controller;

import com.example.pojo.Message;
import com.example.pojo.RoleType;
import com.example.service.JingFangService;
import com.example.service.MessageService;
import com.example.vo.EventMessageVo;
import com.example.vo.EventType;
import com.example.vo.QueryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gzx
 * @date 2023/11/17
 * @Description 用于给用户发送消息
 */
@RestController
@RequestMapping("/sse")
@Slf4j
public class SseController {

    @Resource
    JingFangService jingFangService;

    @Resource
    MessageService messageService;

    Map<String, SseEmitter> emitters = new HashMap<>();

    @GetMapping("/{chatId}")
    public SseEmitter subscribe(@PathVariable String chatId) {
        // 设置过期时间为5分钟
        SseEmitter emitter = new SseEmitter(1000_000L);
        emitter.onCompletion(() -> emitters.remove(chatId));
        emitter.onTimeout(() -> emitters.remove(chatId));
        emitters.put(chatId, emitter);

        return emitter;
    }

    @PostMapping(value = "/chat/{chatId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void sendMessage(@PathVariable String chatId,
                            @RequestBody QueryVo queryVo) {

        SseEmitter emitter = emitters.get(chatId);
        if (emitter == null)
            return;
        ResponseEntity<Map> diagnosis = jingFangService.getDiagnosis(queryVo);
        System.out.println(diagnosis.getStatusCode());
        // 如果响应的状态码不是200，则返回错误
        if (diagnosis.getStatusCode() != HttpStatus.OK) {
            EventMessageVo errorEvent = new EventMessageVo().setEvent(EventType.ERROR).setData("服务出错，请重试！");
            try {
                // 使用eventBuilder发送消息
                SseEmitter.SseEventBuilder eventBuilder = SseEmitter.event()
                        .name(EventType.ERROR)
                        .data(errorEvent.toJson());
                emitter.send(eventBuilder);
            } catch (IOException e) {
                emitter.complete();
                emitters.remove(chatId);
            }
        }
        // 将用户发来的新消息保存
        String userMessageText = queryVo.getText();
        String userMessageId = queryVo.getMessageId();
        Message userMessage = new Message().setChatId(chatId)
                .setMessageId(userMessageId)
                .setContent(userMessageText)
                .setRole(RoleType.USER)
                .setMetadata("")
                // 用户消息设置的比服务器响应消息早1ms，这样可以保证前端正常显示
                .setCreateTime(new Date(new Date().getTime() - 1000))
                .setIsDelete(0);
        // 保存消息
        messageService.createMessage(userMessage);

        Map body = diagnosis.getBody();
//        System.out.println(body);
        // 将新的assistant的消息保存
        String assistantMessageText = (String) body.get("response");;
        String metadata = "";
        String assistantMessageId = UUID.randomUUID().toString();
        Message assistantMessage = new Message().setChatId(chatId)
                .setMessageId(assistantMessageId)
                .setContent(assistantMessageText)
                .setRole(RoleType.ASSISTANT)
                .setMetadata(metadata)
                .setCreateTime(new Date())
                .setIsDelete(0);
        messageService.createMessage(assistantMessage);

        // 先返回messageId
        HashMap<Object, Object> userMessageIdMap = new HashMap<>();
        userMessageIdMap.put("userMessageId", userMessageId);

        HashMap<Object, Object> assistantMessageIdMap = new HashMap<>();
        assistantMessageIdMap.put("assistantMessageId", assistantMessageId);
        try {
            SseEmitter.SseEventBuilder userMessageIdEventBuilder = SseEmitter.event()
                    .name(EventType.MESSAGE)
                    .data(new EventMessageVo().setEvent(EventType.MESSAGE).setData(userMessageIdMap).toJson());
            emitter.send(userMessageIdEventBuilder);

            SseEmitter.SseEventBuilder assistantMessageIdEventBuilder = SseEmitter.event()
                    .name(EventType.MESSAGE)
                    .data(new EventMessageVo().setEvent(EventType.MESSAGE).setData(assistantMessageIdMap).toJson());
            emitter.send(assistantMessageIdEventBuilder);
        } catch (IOException e) {
            emitter.complete();
            emitters.remove(chatId);
        }

        // 然后逐字符地返回响应
        for (char c : assistantMessageText.toCharArray()) {
            HashMap<Object, Object> eventMessageMap = new HashMap<>();
            eventMessageMap.put("delta", c);
            try {
                SseEmitter.SseEventBuilder messageEventBuilder = SseEmitter.event()
                        .name(EventType.MESSAGE)
                        .data(new EventMessageVo().setEvent(EventType.MESSAGE).setData(eventMessageMap).toJson());
                emitter.send(messageEventBuilder);
                Thread.sleep(100);
            } catch (IOException | InterruptedException e) {
                emitter.complete();
                emitters.remove(chatId);
            }
        }

        // 最后再次返回整个响应
        try {
            SseEmitter.SseEventBuilder endEventBuilder = SseEmitter.event()
                    .name(EventType.END)
                    .data(new EventMessageVo().setEvent(EventType.END).setData(assistantMessageText));
            emitter.send(endEventBuilder);
        } catch (IOException e) {
            emitter.complete();
            emitters.remove(chatId);
        }
        // 最后断开连接
        emitter.complete();
    }
}
