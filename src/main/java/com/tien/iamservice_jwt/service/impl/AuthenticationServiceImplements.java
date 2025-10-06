package com.tien.iamservice_jwt.service.impl;

import com.tien.iamservice_jwt.dto.request.AuthenticationRequest;
import com.tien.iamservice_jwt.dto.response.AuthenticationResponse;
import com.tien.iamservice_jwt.service.AuthenticationService;
import com.tien.iamservice_jwt.service.JwtService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImplements implements AuthenticationService {
    AuthenticationManager authenticationManager;
    CustomeUserDetailService customeUserDetailService;
    JwtService jwtService;
    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPass()));
        if (auth.isAuthenticated()) {
            AuthenticationResponse authenticationRespose = new AuthenticationResponse();
            authenticationRespose.setCheckLogin(true);
            authenticationRespose.setToken(jwtService.generateToken(customeUserDetailService.loadUserByUsername(authenticationRequest.getEmail())));
            return authenticationRespose;
        }
        throw new RuntimeException("Invalid login");
    }
}
