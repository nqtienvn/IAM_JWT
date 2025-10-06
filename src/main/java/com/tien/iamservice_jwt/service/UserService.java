package com.tien.iamservice_jwt.service;

import com.tien.iamservice_jwt.dto.request.UserRegisterRequest;
import com.tien.iamservice_jwt.dto.response.UserRegisterResponseInformation;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    UserRegisterResponseInformation register(UserRegisterRequest userRegisterRequest);
}