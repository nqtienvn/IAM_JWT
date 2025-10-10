package com.tien.iamservice_jwt.service.impl;

import com.tien.iamservice_jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    //hàm thể hiện là dùng thằng UserDetailsService để lấy từ db và trả về thăng UserDetail đó
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        return userRepository.findUserByEmailIs(name).orElseThrow(() -> new UsernameNotFoundException(name));
    }
}
