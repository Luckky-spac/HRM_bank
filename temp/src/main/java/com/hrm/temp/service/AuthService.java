package com.hrm.temp.service;

import java.time.LocalDateTime;  // เพิ่มบรรทัดนี้
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hrm.temp.dto.LoginRequest;
import com.hrm.temp.dto.RegisterRequest;
import com.hrm.temp.entity.User;
import com.hrm.temp.repository.UserRepository;
import com.hrm.temp.security.JwtService;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUserId("USR-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // อัปเดต LAST_LOGIN
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);  // สำคัญ!

        return jwtService.generateToken(user.getUsername());
    }
}