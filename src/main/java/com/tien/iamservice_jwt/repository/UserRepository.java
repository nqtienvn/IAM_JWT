package com.tien.iamservice_jwt.repository;

import com.tien.iamservice_jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByEmail(String email);
}
