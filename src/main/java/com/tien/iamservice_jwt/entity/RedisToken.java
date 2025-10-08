package com.tien.iamservice_jwt.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("redisHashToken") //gion kieu danh dau @Entity de no map vao trong redis
//redisHashToken : la key name, con nhung field trong entity la value
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RedisToken {
    @Id
    private String jwtId;
    @TimeToLive(unit = TimeUnit.MILLISECONDS) //chi dinh thoi gian song theo ngay
    //chi ding ttl cho redis de tu dong xoa neu het han
    private long expired;
}
