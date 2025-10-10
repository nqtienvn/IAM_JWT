package com.tien.iamservice_jwt.service;

import com.tien.iamservice_jwt.dto.request.AuthenticationRequest;
import com.tien.iamservice_jwt.dto.request.ChangePassWordRequest;
import com.tien.iamservice_jwt.dto.request.ConfirmPasswordRequest;
import com.tien.iamservice_jwt.dto.response.AuthenticationResponse;
import com.tien.iamservice_jwt.dto.response.ChangePassWordResponse;

public interface AuthenticationService {
    AuthenticationResponse login(AuthenticationRequest authenticationRequest);
    AuthenticationResponse logout(String token);
    ChangePassWordResponse changePassword(ChangePassWordRequest changePassWordRequest, String token);
    void forgotPassWord (String email);
    void confirmPassWord (ConfirmPasswordRequest confirmPasswordRequest);
}
