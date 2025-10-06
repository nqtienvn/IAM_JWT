package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.dto.request.AuthenticationRequest;
import com.tien.iamservice_jwt.dto.response.AuthenticationResponse;
import com.tien.iamservice_jwt.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest userLogin) {
            return ResponseEntity.ok(authenticationService.login(userLogin));
    }
}
