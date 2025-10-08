package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class CloudinaryController {
    private final CloudinaryService cloudinaryService;
    @GetMapping("/upload")
    public String upload(@RequestParam MultipartFile multipartFile) throws IOException {
        cloudinaryService.uploadAvatar(multipartFile);
        return "done";
    }
}
