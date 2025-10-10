package com.tien.iamservice_jwt.entity;

import org.springframework.data.annotation.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("RedisOTP")
@Builder
public class RedisOTP {
    @Id
    private String otp;
    private String subject;
    @TimeToLive(unit = TimeUnit.SECONDS)
    private long timeToLive;
}

