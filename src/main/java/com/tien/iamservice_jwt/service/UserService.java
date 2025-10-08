package com.tien.iamservice_jwt.service;

import com.tien.iamservice_jwt.dto.request.UserRegisterRequest;
import com.tien.iamservice_jwt.dto.response.UserRegisterResponseInformation;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    UserRegisterResponseInformation register(UserRegisterRequest userRegisterRequest);
    List<UserRegisterResponseInformation> getAll();
    UserRegisterResponseInformation getMyInfor();
    UserRegisterResponseInformation uploadProfile(MultipartFile image, String mail);
}