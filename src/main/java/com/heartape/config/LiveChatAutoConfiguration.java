package com.heartape.config;

import com.heartape.connect.ConnectionManager;
import com.heartape.connect.MemoryConnectionManager;
import com.heartape.connect.SseConnection;
import com.heartape.filter.FilterChain;
import com.heartape.filter.MemoryBlackListFilter;
import com.heartape.filter.SimpleSerialFilterChain;
import com.heartape.flow.FlowManager;
import com.heartape.flow.MemoryFlowManager;
import com.heartape.repository.BulletChat;
import com.heartape.repository.BulletChatRepository;
import com.heartape.repository.MemoryBulletChatRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableConfigurationProperties(LiveChatProperties.class)
public class LiveChatAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LiveChatConfigurer liveChatConfigurer(FlowManager flowManager,
                                                 ConnectionManager<SseConnection> connectionManager,
                                                 BulletChatRepository bulletChatRepository,
                                                 FilterChain<BulletChat> filterChain){
        return new LiveChatConfigurer.LiveChatConfigurerBuilder()
                .bulletChatRepository(bulletChatRepository)
                .flowManager(flowManager)
                .connectionManager(connectionManager)
                .filterChain(filterChain)
                .build();
    }

    @Configuration
    protected static class LiveChatBasicConfigurer {

        @Bean
        @ConditionalOnMissingBean
        public FlowManager flowManager(){
            return new MemoryFlowManager(1000, 1);
        }

        @Bean
        @ConditionalOnMissingBean
        public ConnectionManager<SseConnection> connectionManager(){
            return new MemoryConnectionManager();
        }

        @Bean
        @ConditionalOnMissingBean
        public BulletChatRepository bulletChatRepository(){
            return new MemoryBulletChatRepository();
        }

        @Bean
        @ConditionalOnMissingBean
        public FilterChain<BulletChat> filterChain(LiveChatProperties liveChatProperties){
            List<Long> black;
            LiveChatProperties.Auth auth = liveChatProperties.getAuth();
            if (auth == null){
                black = new ArrayList<>();
            } else {
                black = auth.getBlack();
                if (black == null){
                    black = new ArrayList<>();
                }
            }

            return SimpleSerialFilterChain
                    .builder()
                    .filter(new MemoryBlackListFilter(new HashSet<>(black)))
                    .build();
        }

    }

}
