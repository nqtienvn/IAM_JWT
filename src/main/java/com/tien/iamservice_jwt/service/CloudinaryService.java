package com.tien.iamservice_jwt.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface CloudinaryService {
    String uploadAvatar(MultipartFile file) throws IOException;
}
