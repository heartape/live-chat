package com.heartape.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


/**
 * ws://localhost:8080/chat
 */
@Slf4j
@Component
@ServerEndpoint("/chat")
public class BulletChatWebsocketServer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private long uid;

    /**
     * 连接成功
     */
    @SneakyThrows
    @OnOpen
    public void onOpen(Session session) {
        log.info("开启连接");
        // todo:注册到连接管理器
    }

    /**
     * 连接关闭
     */
    @SneakyThrows
    @OnClose
    public void onClose() {
        log.info("关闭连接");
    }

    /**
     * 接收到消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info(message);
    }

    /**
     * 异常
     */
    @OnError
    public void onError(Throwable throwable) {
        log.info("连接异常");
    }
}
