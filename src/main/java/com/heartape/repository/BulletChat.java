package com.heartape.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BulletChat {
    private Long id;
    private Long uid;
    private String content;
    private String timestamp;
}
