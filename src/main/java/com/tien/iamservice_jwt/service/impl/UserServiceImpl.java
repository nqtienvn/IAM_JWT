package com.tien.iamservice_jwt.service.impl;

import com.tien.iamservice_jwt.dto.request.UserRegisterRequest;
import com.tien.iamservice_jwt.dto.response.UserRegisterResponseInformation;
import com.tien.iamservice_jwt.entity.User;
import com.tien.iamservice_jwt.mapper.UserMapper;
import com.tien.iamservice_jwt.repository.UserRepository;
import com.tien.iamservice_jwt.service.CloudinaryService;
import com.tien.iamservice_jwt.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    CloudinaryService cloudinaryService;

    @Override
    public UserRegisterResponseInformation register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsUserByEmail(userRegisterRequest.getEmail())) {
            throw new RuntimeException("Email is existed");
        }
        User user = userMapper.toUser(userRegisterRequest);
        user.setPass(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return userMapper.toUserRegisterResponseInformation(user);
    }

    @Override
    public List<UserRegisterResponseInformation> getAll() {
        List<User> users = userRepository.findAll();
        List<UserRegisterResponseInformation> listUserResponse = new ArrayList<>();
        for (User user : users) {
            listUserResponse.add(userMapper.toUserRegisterResponseInformation(user));
        }
        return listUserResponse;
    }

    @Override
    public UserRegisterResponseInformation getMyInfor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findUserByEmailIs(email).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserRegisterResponseInformation(user);
    }

    @Override
    public UserRegisterResponseInformation uploadProfile(MultipartFile image, String mail) {
        User user = userRepository.findUserByEmailIs(mail).orElseThrow(() -> new RuntimeException("User not found"));
        try {
                String imageUrl = cloudinaryService.uploadAvatar(image);
                user.setProfilePicture(imageUrl);
            return userMapper.toUserRegisterResponseInformation(userRepository.save(user));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload course media", e);
        }
    }
}
