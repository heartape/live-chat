package com.heartape.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartape.repository.BulletChat;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/live/chat")
@AllArgsConstructor
public class ProducerController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public String single(@RequestBody String message) {
        String timestamp = Long.toString(System.currentTimeMillis());
        BulletChat bulletChat = new BulletChat(1L, 1L, message, timestamp);
        ObjectMapper objectMapper = new ObjectMapper();
        String s;
        try {
            s = objectMapper.writeValueAsString(bulletChat);
        } catch (JsonProcessingException e) {
            return "error";
        }
        kafkaTemplate.send("live.chat", s);
        return "success";
    }

}
