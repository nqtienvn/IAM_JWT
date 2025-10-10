package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.dto.request.UserRegisterRequest;
import com.tien.iamservice_jwt.dto.response.ApiResponse;
import com.tien.iamservice_jwt.dto.response.UserRegisterResponseInformation;
import com.tien.iamservice_jwt.exception.AppException;
import com.tien.iamservice_jwt.exception.ErrorCode;
import com.tien.iamservice_jwt.service.SendMailService;
import com.tien.iamservice_jwt.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j(topic = "User Controller")
public class UserController {
    UserService userService;
    SendMailService sendMailService;

    @PostMapping
    public ApiResponse<UserRegisterResponseInformation> register(
            @RequestBody UserRegisterRequest userRegisterRequest) {
        try {
            UserRegisterResponseInformation response =
                    userService.register(userRegisterRequest);
            try {
                sendMailService.send(
                        userRegisterRequest.getEmail(),
                        "Hello " + userRegisterRequest.getFirstName(),
                        "Bạn đã đăng ký ứng dụng của chúng tôi. Cảm ơn bạn!"
                );
            } catch (Exception mailEx) {
                log.info("Không thể gửi email xác nhận: " + mailEx.getMessage());
            }
            return ApiResponse.<UserRegisterResponseInformation>builder()
                    .code(200)
                    .message("success")
                    .result(response)
                    .build();
        } catch (Exception e) {
            log.info("email is existed");
            throw new AppException(ErrorCode.INVALID_EMAIL);
        }
    }

    @GetMapping
    public ApiResponse<List<UserRegisterResponseInformation>> getAll() {
        return ApiResponse.<List<UserRegisterResponseInformation>>builder()
                .code(200)
                .message("success")
                .result(userService.getAll())
                .build();
    }

    @GetMapping("/my-info")
    public ApiResponse<UserRegisterResponseInformation> getMyInfo() {
        return ApiResponse.<UserRegisterResponseInformation>builder()
                .code(200)
                .message("success")
                .result(userService.getMyInfor())
                .build();
    }

    @PostMapping("/avatar")
    public ApiResponse<UserRegisterResponseInformation> uploadProfilePicture(@RequestParam MultipartFile file, @RequestParam String mail) {
        return ApiResponse.<UserRegisterResponseInformation>builder()
                .code(200)
                .message("success")
                .result(userService.uploadProfile(file, mail))
                .build();
    }
}
