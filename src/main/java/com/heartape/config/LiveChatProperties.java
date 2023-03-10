package com.heartape.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties("live.chat")
public class LiveChatProperties {

    private Flow flow;

    private Auth auth;

    @Getter
    @Setter
    public static class Flow {
        private Integer count;
    }

    @Getter
    @Setter
    public static class Auth {
        private List<Long> black;
    }

}
