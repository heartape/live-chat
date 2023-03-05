package com.heartape.flow;

import com.heartape.BulletChat;
import com.heartape.connect.Connection;
import com.heartape.connect.ConnectionManager;
import com.heartape.connect.SseConnection;
import com.heartape.queue.BulletChatQueue;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 弹幕流重点:
 * <li>推拉结合</li>
 * <li>长连接降级</li>
 */
public class BulletChatFlow {

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            10,
            10,
            100,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(10),
            new ThreadPoolExecutor.AbortPolicy()
    );

    /**
     * 开启一个弹幕流线程
     */
    public void start(){
        int activeCount = threadPoolExecutor.getActiveCount();
        if (activeCount >= 10){
            return;
        }
        threadPoolExecutor.execute(() -> {
            BulletChatQueue bulletChatQueue = BulletChatQueue.getInstance();
            for (;;){
                if (!bulletChatQueue.isEmpty()){
                    List<BulletChat> bulletChats = bulletChatQueue.poll();
                    String timestamp = Long.toString(System.currentTimeMillis());
                    ConnectionManager connectionManager = ConnectionManager.getInstance();
                    for (Connection connection : connectionManager.pickAll()) {
                        if (connection instanceof SseConnection sseConnection){
                            try {
                                sseConnection.getSseEmitter().send(SseEmitter.event()
                                        .id(timestamp)
                                        .data(bulletChats)
                                );
                            } catch (IllegalStateException e) {
                                connectionManager.logout(sseConnection.getUid());
                            } catch (IOException ignored) {}
                        }
                    }
                } else {
                    System.out.println("Empty");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
