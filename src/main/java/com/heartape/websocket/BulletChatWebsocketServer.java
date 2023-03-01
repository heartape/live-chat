package com.heartape.websocket;

import com.heartape.BulletChat;
import com.heartape.queue.BulletChatQueue;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ws://localhost:8080/chat
 */
@Slf4j
@Component
@ServerEndpoint("/chat")
public class BulletChatWebsocketServer {
    private byte status = 0;

    /**
     * 连接成功
     */
    @SneakyThrows
    @OnOpen
    public void onOpen(Session session) {
        log.info("开启连接");
        while (this.status >= 0 && session.isOpen()){
            List<BulletChat> bulletChats = BulletChatQueue.getInstance().poll();
            if (bulletChats != null){
                session.getAsyncRemote().sendObject(bulletChats);
                log.info("发送弹幕");
            }
            // log.info("-----------------------");
            TimeUnit.MILLISECONDS.sleep(1000);
        }
        session.close();
    }

    /**
     * 连接关闭
     */
    @SneakyThrows
    @OnClose
    public void onClose(Session session) {
        session.close();
        log.info("关闭连接");
    }

    /**
     * 接收到消息
     */
    @OnMessage
    public void onMessage(String text) {
        // System.out.println(Arrays.toString(bytes));
        // String text = new String(bytes, StandardCharsets.UTF_8);
        log.info(text);
        switch (text) {
            case "-1" -> this.status = -1;
            case "0" -> this.status = 0;
            case "1" -> this.status = 1;
            case "2" -> this.status = 2;
        }
    }

    /**
     * 异常
     */
    @OnError
    public String onError(Throwable throwable) {
        return "异常";
    }
}
