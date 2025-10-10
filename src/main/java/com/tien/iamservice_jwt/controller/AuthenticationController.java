package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.dto.request.AuthenticationRequest;
import com.tien.iamservice_jwt.dto.request.ChangePassWordRequest;
import com.tien.iamservice_jwt.dto.request.ConfirmPasswordRequest;
import com.tien.iamservice_jwt.dto.response.ApiResponse;
import com.tien.iamservice_jwt.dto.response.AuthenticationResponse;
import com.tien.iamservice_jwt.dto.response.ChangePassWordResponse;
import com.tien.iamservice_jwt.service.AuthenticationService;
import com.tien.iamservice_jwt.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    JwtService jwtService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest userLogin) {
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("success")
                .result(authenticationService.login(userLogin))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<AuthenticationResponse> logout(@RequestHeader("Authorization") String bearertoken) {
        log.info("token:{}", bearertoken);
        String token = bearertoken.substring(7);
        return ApiResponse.<AuthenticationResponse>builder()
                .code(200)
                .message("success")
                .result(authenticationService.logout(token))
                .build();
    }

    @PostMapping("/refresh-token")
    public ApiResponse<String> refreshToken(@RequestParam String refreshToken) {
        return ApiResponse.<String>builder()
                .code(200)
                .message("success")
                .result(jwtService.refreshAcessToken(refreshToken))
                .build();
    }

    @PostMapping("/change-password")
    public ApiResponse<ChangePassWordResponse> changePassWord(@RequestBody ChangePassWordRequest changePassWordRequest,
                                                              @RequestHeader("Authorization") String bearertoken) {
        String token = bearertoken.substring(7);
        return ApiResponse.<ChangePassWordResponse>builder()
                .code(200)
                .message("success")
                .result(authenticationService.changePassword(changePassWordRequest, token))
                .build();
    }

    @PostMapping("/forgot-password")
    public ApiResponse<String> forgot(@RequestParam String email) {
        authenticationService.forgotPassWord(email);
        return ApiResponse.<String>builder()
                .code(200)
                .message("success")
                .build();
    }

    @PostMapping("/confirm-password")
    public ApiResponse<String> cofirm(@RequestBody ConfirmPasswordRequest confirmPasswordRequest) {
        authenticationService.confirmPassWord(confirmPasswordRequest);
        return ApiResponse.<String>builder()
                .code(200)
                .message("success")
                .build();
    }
}
