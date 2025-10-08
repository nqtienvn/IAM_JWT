package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.dto.request.UserRegisterRequest;
import com.tien.iamservice_jwt.dto.response.UserRegisterResponseInformation;
import com.tien.iamservice_jwt.service.SendMailService;
import com.tien.iamservice_jwt.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<UserRegisterResponseInformation> register(
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
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.info("email is existed");
            return ResponseEntity.badRequest().body(new UserRegisterResponseInformation());
        }
    }

    @GetMapping
    public ResponseEntity<List<UserRegisterResponseInformation>> getAll() {
        return ResponseEntity.ok(userService.getAll());
    }
    @GetMapping("/my-infor")
    public ResponseEntity<UserRegisterResponseInformation> getMyInfor() {
        return ResponseEntity.ok(userService.getMyInfor());
    }
    @PostMapping("/avatar")
    public ResponseEntity<UserRegisterResponseInformation> uploadProfilePicture(@RequestParam MultipartFile file, @RequestParam String mail) {
        return ResponseEntity.ok(userService.uploadProfile(file, mail));
    }
}
