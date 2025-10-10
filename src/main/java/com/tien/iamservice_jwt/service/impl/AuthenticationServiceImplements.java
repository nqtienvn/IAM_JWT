package com.tien.iamservice_jwt.service.impl;

import com.tien.iamservice_jwt.dto.request.AuthenticationRequest;
import com.tien.iamservice_jwt.dto.request.ChangePassWordRequest;
import com.tien.iamservice_jwt.dto.request.ConfirmPasswordRequest;
import com.tien.iamservice_jwt.dto.response.AuthenticationResponse;
import com.tien.iamservice_jwt.dto.response.ChangePassWordResponse;
import com.tien.iamservice_jwt.entity.RedisOTP;
import com.tien.iamservice_jwt.entity.RedisToken;
import com.tien.iamservice_jwt.entity.User;
import com.tien.iamservice_jwt.exception.AppException;
import com.tien.iamservice_jwt.exception.ErrorCode;
import com.tien.iamservice_jwt.repository.RedisOtpRepository;
import com.tien.iamservice_jwt.repository.RedisRepository;
import com.tien.iamservice_jwt.repository.UserRepository;
import com.tien.iamservice_jwt.service.AuthenticationService;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "Auth_service_implements")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImplements implements AuthenticationService {
    AuthenticationManager authenticationManager;
    CustomUserDetailService customeUserDetailService;
    JwtService jwtService;
    RedisRepository redisRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    SendMailService sendMailService;
    RedisOtpRepository redisOtpRepository;

    @Override
    public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
        Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest
                        .getEmail(), authenticationRequest.getPass()));
        if (auth.isAuthenticated()) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setCheckLogin(true);
            authenticationResponse.setAcessToken(jwtService
                    .generateAccessToken(customeUserDetailService
                            .loadUserByUsername(authenticationRequest
                                    .getEmail())));
            String refreshToken = jwtService
                    .generateRefreshToken(customeUserDetailService
                            .loadUserByUsername(authenticationRequest
                                    .getEmail()));
            authenticationResponse.setRefreshToken(refreshToken);
            log.info(jwtService.extracId(refreshToken));
            RedisToken redisToken = RedisToken
                    .builder()
                    .jwtId(jwtService.extracId(refreshToken))
                    .expired(jwtService.extractExpiration(refreshToken).getTime() - System.currentTimeMillis())
                    .type("refreshToken")
                    .subject(authenticationRequest.getEmail())
                    .build();
            redisRepository.save(redisToken);
            //lay thanh refresh token cu kieu gi nhi
            List<RedisToken> redisTokenList = (List<RedisToken>) redisRepository.findAll();
            for (RedisToken token : redisTokenList) {
                if (token == null) continue;
                if (token.getType().equals("refreshToken")
                        && token.getSubject().equals(authenticationRequest.getEmail())
                        && !token.getJwtId().equals(jwtService.extracId(refreshToken))) {
                    redisRepository.delete(token);
                }
            }
            //chac chi co truyen refresh Token cu bang body a
            //thieu la neu co redisToken cu thi can xoa di
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
        RedisToken redisToken = RedisToken
                .builder()
                .jwtId(tokenId)
                .expired(jwtService.extractExpiration(token).getTime() - System.currentTimeMillis())
                .type("accessToken")
                .subject(jwtService.extractEmail(token))
                .build();
        redisRepository.save(redisToken);
        List<RedisToken> redisTokenList = (List<RedisToken>) redisRepository.findAll();
        for (RedisToken tokens : redisTokenList) {
            if (tokens.getType().equals("refreshToken")
                    && tokens.getSubject().equals(jwtService.extractEmail(token))) { //k sao ca vi no van la cua thang nay dang ki thoi
                redisRepository.delete(tokens);
            }
        }
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setCheckLogin(true);
        return authenticationResponse;
    }

    @Override
    public ChangePassWordResponse changePassword(ChangePassWordRequest changePassWordRequest) {
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
        List<RedisToken> redisTokenList = (List<RedisToken>) redisRepository.findAll();

        for (RedisToken redisToken : redisTokenList) {
            if (redisToken == null) continue;
            if (redisToken.getType().equals("refreshToken") && redisToken.getSubject().equals(email)) {
                redisRepository.delete(redisToken);
            }
        }
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
            RedisOTP redis = RedisOTP.builder() //sau chi can tim dc ra trong redis la oke roi
                    .otp(otp)
                    .timeToLive(360)
                    .subject(email)
                    .build();
            redisOtpRepository.save(redis);
            sendMailService.send(email, "Code to reset password - 360s dem nguoc bat dau", otp);
            return;
        }
        throw new AppException(ErrorCode.INVALID_EMAIL);
    }

    @Override
    public void confirmPassWord(ConfirmPasswordRequest confirmPasswordRequest) {
        if (redisOtpRepository.findById(confirmPasswordRequest.getOtp()).isPresent()) {
            List<RedisOTP> listOfRedisOtp = (List<RedisOTP>) redisOtpRepository.findAll();
            for (RedisOTP redisOTP : listOfRedisOtp) {
                if (redisOTP == null) continue;
                if (redisOTP.getSubject().equals(confirmPasswordRequest.getEmail())) {
                    User user = userRepository.findUserByEmailIs(confirmPasswordRequest.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
                    user.setPass(passwordEncoder.encode(confirmPasswordRequest.getNewPassword()));
                    userRepository.save(user);
                    log.info(confirmPasswordRequest.getNewPassword());
                    return;
                }
            }
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }
        throw new AppException(ErrorCode.INVALID_OTP);
    }
}

