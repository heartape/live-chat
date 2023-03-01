package com.heartape.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heartape.BulletChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/live/chat")
public class ProducerController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping
    public String single(@RequestBody String message) {
        BulletChat bulletChat = new BulletChat(1L, 1L, message);
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
