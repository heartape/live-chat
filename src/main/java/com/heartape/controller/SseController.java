package com.heartape.controller;

import com.heartape.connect.MemoryConnectionManager;
import com.heartape.connect.SseConnection;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse")
public class SseController {

    @CrossOrigin
    @GetMapping("/chat")
    public SseEmitter create(@RequestParam Long uid){
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> System.out.println("Completion"));
        sseEmitter.onTimeout(() -> System.out.println("Timeout"));
        sseEmitter.onError(e -> System.out.println("Error"));
        MemoryConnectionManager.getInstance().register(uid, new SseConnection(uid, sseEmitter));
        return sseEmitter;
    }

    @CrossOrigin
    @DeleteMapping("/chat")
    public void destroy(@RequestParam Long uid){
        MemoryConnectionManager
                .getInstance()
                .logout(uid)
                .getSseEmitter()
                .complete();
    }
}
