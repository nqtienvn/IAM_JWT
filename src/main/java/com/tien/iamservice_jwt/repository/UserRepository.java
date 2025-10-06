package com.tien.iamservice_jwt.repository;

import com.tien.iamservice_jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserByEmail(String email);
    Optional<User> findUserByEmailIs(String email);
}
