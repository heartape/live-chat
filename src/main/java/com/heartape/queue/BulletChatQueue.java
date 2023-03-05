package com.heartape.queue;

import com.heartape.BulletChat;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BulletChatQueue {
    private final Queue<List<BulletChat>> queue = new ConcurrentLinkedQueue<>();

    private final static BulletChatQueue bulletChatQueue = new BulletChatQueue();

    public static BulletChatQueue getInstance(){
        return bulletChatQueue;
    }

    public void offer(List<BulletChat> bulletChats){
        if (bulletChats != null && !bulletChats.isEmpty()){
            queue.offer(bulletChats);
        }
    }

    public List<BulletChat> poll(){
        return queue.isEmpty() ? null : queue.poll();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
