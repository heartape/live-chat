package com.heartape.sse;

import com.heartape.BulletChat;
import com.heartape.queue.BulletChatQueue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping("/sse")
public class SseService {

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            100,
            100,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100),
            new ThreadPoolExecutor.AbortPolicy()
    );

    @CrossOrigin
    @GetMapping("/chat")
    public SseEmitter create(@RequestParam String id){
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitter.onCompletion(() -> System.out.println("Completion"));
        sseEmitter.onTimeout(() -> System.out.println("Timeout"));
        sseEmitter.onError(e -> System.out.println("Error"));
        threadPoolExecutor.execute(() -> {
            try {
                String timestamp = Long.toString(System.currentTimeMillis());
                BulletChatQueue bulletChatQueue = BulletChatQueue.getInstance();
                for (;;){
                    if (!bulletChatQueue.isEmpty()){
                        List<BulletChat> bulletChats = bulletChatQueue.poll();
                        sseEmitter.send(SseEmitter.event()
                                .id(timestamp)
                                .data(bulletChats)
                        );
                    } else {
                        System.out.println("Empty");
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }
}
