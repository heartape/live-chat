package com.heartape.config;

import com.heartape.connect.ConnectionManager;
import com.heartape.connect.SseConnection;
import com.heartape.filter.FilterChain;
import com.heartape.flow.FlowManager;
import com.heartape.repository.BulletChat;
import com.heartape.repository.BulletChatRepository;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LiveChatConfigurer {

    private FlowManager flowManager;
    private ConnectionManager<SseConnection> connectionManager;
    private BulletChatRepository bulletChatRepository;
    private FilterChain<BulletChat> filterChain;

}
