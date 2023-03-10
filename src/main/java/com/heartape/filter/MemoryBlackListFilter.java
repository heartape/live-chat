package com.heartape.filter;

import com.heartape.repository.BulletChat;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 黑名单过滤器
 */
public class MemoryBlackListFilter implements Filter<BulletChat> {

    private final Set<Long> blackList = new ConcurrentSkipListSet<>();

    public MemoryBlackListFilter(Set<Long> blackSet) {
        this.blackList.addAll(blackSet);
    }

    @Override
    public boolean permit(BulletChat bulletChat) {
        Long uid = bulletChat.getUid();
        return uid != null && !blackList.contains(uid);
    }

}
