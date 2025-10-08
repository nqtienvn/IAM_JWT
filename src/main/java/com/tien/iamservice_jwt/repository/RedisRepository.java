package com.tien.iamservice_jwt.repository;

import com.tien.iamservice_jwt.entity.RedisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<RedisToken, String> {
}
