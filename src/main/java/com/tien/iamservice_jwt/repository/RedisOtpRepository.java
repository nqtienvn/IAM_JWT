package com.tien.iamservice_jwt.repository;

import com.tien.iamservice_jwt.entity.RedisOTP;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisOtpRepository extends CrudRepository<RedisOTP, String> {
}
