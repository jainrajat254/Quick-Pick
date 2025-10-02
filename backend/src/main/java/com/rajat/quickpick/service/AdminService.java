package com.rajat.quickpick.service;

import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.model.enums.Role;
import com.rajat.quickpick.repository.UserRepository;
import com.rajat.quickpick.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.rajat.quickpick.model.dto.AuthDtos.*;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private static final Logger log = LoggerFactory.getLogger(AdminService.class);

    public AuthResponseDto createAdmin(String email, String password, String fullName) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Admin already exists");
        }

        User admin = new User();
        admin.setFullName(fullName);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin.setEmailVerified(true);
        admin.setPhoneVerified(false);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());

        User savedAdmin = userRepository.save(admin);
        log.info("Admin user created: {}", savedAdmin.getEmail());

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(savedAdmin.getId());
        response.setEmail(savedAdmin.getEmail());
        response.setName(savedAdmin.getFullName());
        response.setRole(savedAdmin.getRole());
        response.setMessage("Admin created successfully");

        return response;
    }

    public AuthResponseDto adminLogin(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User admin = userRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Invalid credentials"));

            if (admin.getRole() != Role.ADMIN) {
                throw new BadRequestException("Access denied. Admin privileges required.");
            }

            String token = jwtUtil.generateToken(userDetails, admin.getId(), admin.getRole().name());

            String refreshToken = jwtUtil.generateRefreshToken(userDetails, admin.getId(),admin.getRole().name());

            AuthResponseDto response = new AuthResponseDto();

            response.setToken(token);
            response.setRefreshToken(refreshToken);
            response.setUserId(admin.getId());
            response.setEmail(admin.getEmail());
            response.setName(admin.getFullName());
            response.setRole(admin.getRole());
            response.setMessage("Admin login successful");

            return response;

        } catch (Exception e) {
            throw new BadRequestException("Invalid admin credentials");
        }
    }
}