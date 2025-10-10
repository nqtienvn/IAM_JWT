package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.service.BaseRedisV2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisControllerTest {
    private final BaseRedisV2Service baseRedisV2Service;

    @GetMapping("/set")
    public String setRedis() {
        baseRedisV2Service.set("key1", "value1");
        return "done";
    }
    @GetMapping("/time-to-live")
    public String setTimeToLive() {
        baseRedisV2Service.setTimeToLive("key1", 20);
        return "done";
    }
}
