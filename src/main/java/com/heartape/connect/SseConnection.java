package com.heartape.connect;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Getter
@AllArgsConstructor
public class SseConnection implements Connection {
    private Long uid;
    private SseEmitter sseEmitter;
}
