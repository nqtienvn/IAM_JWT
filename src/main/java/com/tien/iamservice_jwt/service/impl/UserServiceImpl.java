package com.tien.iamservice_jwt.service.impl;

import ch.qos.logback.classic.util.LevelUtil;
import com.tien.iamservice_jwt.dto.request.UserRegisterRequest;
import com.tien.iamservice_jwt.dto.response.UserRegisterResponseInformation;
import com.tien.iamservice_jwt.entity.User;
import com.tien.iamservice_jwt.enums.Roles;
import com.tien.iamservice_jwt.mapper.UserMapper;
import com.tien.iamservice_jwt.repository.UserRepository;
import com.tien.iamservice_jwt.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    @Override
    public UserRegisterResponseInformation register(UserRegisterRequest userRegisterRequest) {
        if(userRepository.existsUserByEmail(userRegisterRequest.getEmail())) {
            throw new RuntimeException("Email is existed");
        }
        User user = userMapper.toUser(userRegisterRequest);
        user.setPass(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toUserRegisterResponseInformation(user);
    }
}
