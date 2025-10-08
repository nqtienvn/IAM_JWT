package com.tien.iamservice_jwt;

import com.tien.iamservice_jwt.config.RedisProperties;
import com.tien.iamservice_jwt.config.SendGridProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({RedisProperties.class, SendGridProperties.class})
public class IamServiceJwtApplication {
    public static void main(String[] args) {
        SpringApplication.run(IamServiceJwtApplication.class, args);
    }
}
