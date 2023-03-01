package com.heartape.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartape.BulletChat;
import com.heartape.queue.BulletChatQueue;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class MyKafkaListener {

    @KafkaListener(
            id = "live-chat-batch",
            clientIdPrefix = "live-chat",
            groupId = "live-group",
            topics = "live.chat",
            errorHandler = "",
            containerFactory = "",
            batch = "true"
    )
    public void batch(@Payload List<ConsumerRecord<String, String>> payloads) {
        log.info("接收{}条消息", payloads.size());
        ObjectMapper objectMapper = new ObjectMapper();
        List<BulletChat> list = new ArrayList<>();
        for (ConsumerRecord<String, String> payload : payloads) {
            String value = payload.value();
            BulletChat bulletChat;
            try {
                bulletChat = objectMapper.readValue(value, BulletChat.class);
            } catch (JsonProcessingException e) {
                continue;
            }
            list.add(bulletChat);
        }
        BulletChatQueue.getInstance().offer(list);
    }
}
