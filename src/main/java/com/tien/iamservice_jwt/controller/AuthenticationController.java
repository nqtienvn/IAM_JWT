package com.tien.iamservice_jwt.controller;

import com.tien.iamservice_jwt.dto.request.AuthenticationRequest;
import com.tien.iamservice_jwt.dto.response.AuthenticationResponse;
import com.tien.iamservice_jwt.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationController {
    AuthenticationService authenticationService;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest userLogin) {
            return ResponseEntity.ok(authenticationService.login(userLogin));
    }
    @PostMapping("/logout")
    public ResponseEntity<AuthenticationResponse> logout(@RequestHeader("Authorization") String bearertoken) {
            log.info("token:{}", bearertoken);
            String token = bearertoken.substring(7);
            return ResponseEntity.ok(authenticationService.logout(token));
    }
}
