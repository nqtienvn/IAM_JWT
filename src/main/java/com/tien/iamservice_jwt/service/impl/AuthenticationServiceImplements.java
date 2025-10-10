package com.tien.iamservice_jwt.service.impl;

import com.tien.iamservice_jwt.dto.request.AuthenticationRequest;
import com.tien.iamservice_jwt.dto.request.ChangePassWordRequest;
import com.tien.iamservice_jwt.dto.request.ConfirmPasswordRequest;
import com.tien.iamservice_jwt.dto.response.AuthenticationResponse;
import com.tien.iamservice_jwt.dto.response.ChangePassWordResponse;
import com.tien.iamservice_jwt.entity.User;
import com.tien.iamservice_jwt.exception.AppException;
import com.tien.iamservice_jwt.exception.ErrorCode;
import com.tien.iamservice_jwt.repository.RedisRepository;
import com.tien.iamservice_jwt.repository.UserRepository;
import com.tien.iamservice_jwt.service.AuthenticationService;
import com.tien.iamservice_jwt.service.BaseRedisV2Service;
import com.tien.iamservice_jwt.service.JwtService;
import com.tien.iamservice_jwt.service.SendMailService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Auth_service_implements")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImplements implements AuthenticationService {
    AuthenticationManager authenticationManager;
    CustomUserDetailService customerUserDetailService;
    JwtService jwtService;
    RedisRepository redisRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    SendMailService sendMailService;
    BaseRedisV2Service baseRedisV2Service;

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest
                        .getEmail(), authenticationRequest.getPass()));
        if (auth.isAuthenticated()) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setCheckLogin(true);
            authenticationResponse.setAcessToken(jwtService
                    .generateAccessToken(customerUserDetailService
                            .loadUserByUsername(authenticationRequest
                                    .getEmail())));
            String refreshToken = jwtService
                    .generateRefreshToken(customerUserDetailService
                            .loadUserByUsername(authenticationRequest
                                    .getEmail()));
            authenticationResponse.setRefreshToken(refreshToken);
            log.info(jwtService.extracId(refreshToken));
            baseRedisV2Service.set("refreshToken", jwtService.extracId(refreshToken));
            baseRedisV2Service.setTimeToLive("refreshToken", 60 * 60 * 24 * 7);
            return authenticationResponse;
        }
        throw new AppException(ErrorCode.INVALID_LOGIN);
    }

    @Override
    public AuthenticationResponse logout(String token) {
        String tokenId = jwtService.extracId(token);
        if (jwtService.isTokenExpired(token)) { //neu token het han
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setCheckLogin(false);
            return authenticationResponse;
        }
        //chua lam xoa refresh token
        baseRedisV2Service.delete("refreshToken");

        //lam ban them acces token vao redis
        baseRedisV2Service.set(tokenId, "accessToken");
        baseRedisV2Service.setTimeToLive(tokenId, jwtService
                .extractExpiration(token).getTime() - System.currentTimeMillis());

        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setCheckLogin(true);
        return authenticationResponse;
    }

    @Override
    public ChangePassWordResponse changePassword(ChangePassWordRequest changePassWordRequest, String token) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findUserByEmailIs(email).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (!passwordEncoder.matches(changePassWordRequest.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASS_INCORRECT);
        }
        if (!changePassWordRequest.getNewPassword().equals(changePassWordRequest.getConfirmPassword())) {
            throw new AppException(ErrorCode.NOT_MATCH_PASSWORD);
        }
        user.setPass(passwordEncoder.encode(changePassWordRequest.getNewPassword()));
        //xoa refresh token khoi redis
        baseRedisV2Service.delete("refreshToken");
        //them accessToken
        String tokenId = jwtService.extracId(token);
        baseRedisV2Service.set(tokenId, "accessToken");
        baseRedisV2Service.setTimeToLive(tokenId, jwtService
                .extractExpiration(token).getTime() - System.currentTimeMillis());
        userRepository.save(user);
        return ChangePassWordResponse.builder()
                .message("change pass successfully")
                .build();
    }

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateSecureOTP() {
        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }

    //    email, subject, text
    @Override
    public void forgotPassWord(String email) {
        if (userRepository.existsUserByEmail(email)) {
            String otp = generateSecureOTP();
            baseRedisV2Service.set("otp", otp);
            baseRedisV2Service.setTimeToLive("otp", 100);
            sendMailService.send(email, "Code to reset password - 360s dem nguoc bat dau", otp);
            return;
        }
        throw new AppException(ErrorCode.INVALID_EMAIL);
    }

    @Override
    public void confirmPassWord(ConfirmPasswordRequest confirmPasswordRequest) {
        String otpFromRedis = baseRedisV2Service.get("otp").toString();
        if (confirmPasswordRequest.getOtp().equals(otpFromRedis)) {
            User user = userRepository.findUserByEmailIs(confirmPasswordRequest.getEmail()).orElseThrow(()
                    -> new AppException(ErrorCode.USER_NOT_FOUND));
            user.setPass(passwordEncoder.encode(confirmPasswordRequest.getNewPassword()));
            userRepository.save(user);
            log.info(confirmPasswordRequest.getNewPassword());
            return;
        }
        log.info("not to confirm password");
    }
}

