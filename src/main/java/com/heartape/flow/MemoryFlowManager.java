package com.heartape.flow;

import com.heartape.repository.BulletChat;
import com.heartape.connect.Connection;
import com.heartape.connect.MemoryConnectionManager;
import com.heartape.connect.SseConnection;
import com.heartape.repository.MemoryBulletChatRepository;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Setter
@Slf4j
public class MemoryFlowManager implements FlowManager {
    /**
     * 推送间隔
     */
    private int time;

    private final static int THREAD_MAX_COUNT = 10;

    public MemoryFlowManager(int time, int count) {
        this.time = time;
        if (count <= THREAD_MAX_COUNT){
            for (int i = 0; i < count; i++) {
                start();
            }
        }
    }

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
            THREAD_MAX_COUNT,
            THREAD_MAX_COUNT,
            100,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(THREAD_MAX_COUNT),
            new ThreadPoolExecutor.AbortPolicy()
    );

    private volatile int current = 0;

    /**
     * 开启一个弹幕流线程
     */
    @Override
    public void start(){
        int i;
        synchronized (this.threadPoolExecutor) {
            if (this.current >= 10) {
                log.info("启动新弹幕流失败");
                return;
            }
            else {
                i = this.current++;
            }
        }

        this.threadPoolExecutor.execute(() -> {
            MemoryBulletChatRepository memoryBulletChatRepository = MemoryBulletChatRepository.getInstance();
            while (this.current > i){
                if (!memoryBulletChatRepository.isEmpty()){
                    List<BulletChat> bulletChats = memoryBulletChatRepository.select();
                    String timestamp = Long.toString(System.currentTimeMillis());
                    MemoryConnectionManager memoryConnectionManager = MemoryConnectionManager.getInstance();
                    for (Connection connection : memoryConnectionManager.pickAll()) {
                        if (connection instanceof SseConnection sseConnection){
                            try {
                                sseConnection.getSseEmitter().send(SseEmitter.event()
                                        .id(timestamp)
                                        .data(bulletChats)
                                );
                            } catch (IllegalStateException e) {
                                memoryConnectionManager.logout(sseConnection.getUid());
                            } catch (IOException ignored) {}
                        }
                    }
                } else {
                    System.out.println("Empty");
                    try {
                        TimeUnit.MILLISECONDS.sleep(this.time);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    public void stop(int count) {
        if (count >= THREAD_MAX_COUNT) return;
        synchronized (this.threadPoolExecutor) {
            this.current = count;
        }
    }

    @Override
    public void stop() {
        synchronized (this.threadPoolExecutor) {
            this.current = 0;
        }

    }
}
