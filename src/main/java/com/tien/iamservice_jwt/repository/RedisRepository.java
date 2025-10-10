package com.tien.iamservice_jwt.repository;

import com.tien.iamservice_jwt.entity.RedisToken;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RedisRepository extends CrudRepository<RedisToken, String> {

    RedisToken findByTypeAndSubject(String type, String subject);
}
