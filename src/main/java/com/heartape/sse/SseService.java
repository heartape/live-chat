package com.heartape.sse;

import com.heartape.connect.Connection;
import com.heartape.connect.ConnectionManager;
import com.heartape.connect.SseConnection;
import com.heartape.flow.BulletChatFlow;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseService {

    BulletChatFlow bulletChatFlow = new BulletChatFlow();

    @PostConstruct
    private void init(){
        bulletChatFlow.start();
    }

    @CrossOrigin
    @GetMapping("/chat")
    public SseEmitter create(@RequestParam Long uid){
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> System.out.println("Completion"));
        sseEmitter.onTimeout(() -> System.out.println("Timeout"));
        sseEmitter.onError(e -> System.out.println("Error"));
        ConnectionManager.getInstance().register(uid, new SseConnection(uid, sseEmitter));
        return sseEmitter;
    }

    @CrossOrigin
    @DeleteMapping("/chat")
    public void destroy(@RequestParam Long uid){
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        Connection connection = connectionManager.pick(uid);
        if (connection instanceof SseConnection sseConnection){
            sseConnection.getSseEmitter().complete();
        }
        connectionManager.logout(uid);
    }
}
