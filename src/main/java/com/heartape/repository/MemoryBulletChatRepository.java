package com.heartape.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemoryBulletChatRepository implements BulletChatRepository {
    private final Queue<List<BulletChat>> queue = new ConcurrentLinkedQueue<>();

    private final static MemoryBulletChatRepository MEMORY_BULLET_CHAT_REPOSITORY = new MemoryBulletChatRepository();

    public static MemoryBulletChatRepository getInstance(){
        return MEMORY_BULLET_CHAT_REPOSITORY;
    }

    @Override
    public void insert(Collection<BulletChat> bulletChats){
        if (bulletChats != null && !bulletChats.isEmpty()){
            if (bulletChats instanceof List<BulletChat> list){
                queue.offer(list);
            } else {
                queue.offer(new ArrayList<>(bulletChats));
            }

        }
    }

    @Override
    public List<BulletChat> select(){
        return queue.isEmpty() ? null : queue.poll();
    }

    @Override
    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
