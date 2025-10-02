package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.auth.AuthResponseDto;
import com.rajat.quickpick.dto.auth.ChangePasswordDto;
import com.rajat.quickpick.dto.auth.ForgotPasswordDto;
import com.rajat.quickpick.dto.auth.ResetPasswordDto;
import com.rajat.quickpick.dto.user.RegisterUserDto;
import com.rajat.quickpick.dto.vendor.RegisterVendor;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.*;
import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.repository.EmailVerificationTokenRepository;
import com.rajat.quickpick.repository.PasswordResetTokenRepository;
import com.rajat.quickpick.repository.UserRepository;
import com.rajat.quickpick.repository.VendorRepository;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.utils.Secrets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VendorRepository vendorRepository;
    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    public AuthResponseDto registerUser(RegisterUserDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        if (userRepository.existsByPhone(registrationDto.getPhone())) {
            throw new BadRequestException("Phone number already registered");
        }

        if (userRepository.existsByStudentId(registrationDto.getStudentId())) {
            throw new BadRequestException("Student ID already registered");
        }

        // Create new user
        User user = new User();
        user.setFullName(registrationDto.getFullName());
        user.setGender(registrationDto.getGender());
        user.setPhone(registrationDto.getPhone());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setCollegeName(registrationDto.getCollegeName());
        user.setDepartment(registrationDto.getDepartment());
        user.setStudentId(registrationDto.getStudentId());
        user.setRole(Role.STUDENT);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        createAndSendVerificationToken(savedUser.getEmail(), Role.STUDENT);

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setName(savedUser.getFullName());
        response.setRole(savedUser.getRole());
        response.setMessage("Registration successful. Please check your email to verify your account.");

        return response;
    }

    public AuthResponseDto registerVendor(RegisterVendor registrationDto) {
        if (vendorRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BadRequestException("Email already registered");
        }

        if (vendorRepository.existsByPhone(registrationDto.getPhone())) {
            throw new BadRequestException("Phone number already registered");
        }

        if (vendorRepository.existsByGstNumber(registrationDto.getGstNumber())) {
            throw new BadRequestException("GST number already registered");
        }

        Vendor vendor = new Vendor();
        vendor.setVendorName(registrationDto.getVendorName());
        vendor.setStoreName(registrationDto.getStoreName());
        vendor.setEmail(registrationDto.getEmail());
        vendor.setPhone(registrationDto.getPhone());
        vendor.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        vendor.setAddress(registrationDto.getAddress());
        vendor.setCollegeName(registrationDto.getCollegeName());
        vendor.setVendorDescription(registrationDto.getVendorDescription());
        vendor.setFoodCategories(registrationDto.getFoodCategories());
        vendor.setGstNumber(registrationDto.getGstNumber());
        vendor.setLicenseNumber(registrationDto.getLicenseNumber());
        vendor.setFoodLicenseNumber(registrationDto.getFoodLicenseNumber());
        vendor.setRole(Role.VENDOR);
        vendor.setEmailVerified(false);
        vendor.setPhoneVerified(false);
        vendor.setCreatedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());

        Vendor savedVendor = vendorRepository.save(vendor);

        createAndSendVerificationToken(savedVendor.getEmail(), Role.VENDOR);

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(savedVendor.getId());
        response.setEmail(savedVendor.getEmail());
        response.setName(savedVendor.getVendorName());
        response.setRole(savedVendor.getRole());
        response.setMessage("Registration successful. Please check your email to verify your account.");

        return response;
    }

    public AuthResponseDto login(String email, String password, String userType) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if ("USER".equals(userType)) {
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new BadRequestException("Invalid credentials"));

                if (!user.isEmailVerified()) {
                    throw new BadRequestException("Please verify your email first");
                }

                String token = jwtUtil.generateToken(userDetails, user.getId(), user.getRole().name());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId(), user.getEmail(), "USER");

                AuthResponseDto response = new AuthResponseDto();
                response.setToken(token);
                response.setRefreshToken(refreshToken.getToken());
                response.setUserId(user.getId());
                response.setEmail(user.getEmail());
                response.setName(user.getFullName());
                response.setRole(user.getRole());
                response.setMessage("Login successful");

                return response;

            } else if ("VENDOR".equals(userType)) {
                Vendor vendor = vendorRepository.findByEmail(email)
                        .orElseThrow(() -> new BadRequestException("Invalid credentials"));

                if (!vendor.isEmailVerified()) {
                    throw new BadRequestException("Please verify your email first");
                }

                String token = jwtUtil.generateToken(userDetails, vendor.getId(), vendor.getRole().name());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(vendor.getId(), vendor.getEmail(), "VENDOR");

                AuthResponseDto response = new AuthResponseDto();
                response.setToken(token);
                response.setRefreshToken(refreshToken.getToken());
                response.setUserId(vendor.getId());
                response.setEmail(vendor.getEmail());
                response.setName(vendor.getVendorName());
                response.setRole(vendor.getRole());
                response.setMessage("Login successful");

                return response;
            } else {
                throw new BadRequestException("Invalid user type");
            }

        } catch (Exception e) {
            throw new BadRequestException("Invalid credentials");
        }
    }

    public void verifyEmail(String token, String type) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid verification token"));

        if (verificationToken.isUsed()) {
            throw new BadRequestException("Token already used");
        }

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token has expired");
        }

        Role typeRole;
        try {
            typeRole = Role.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid token type");
        }

        if (!verificationToken.getUserType().equals(typeRole)) {
            throw new BadRequestException("Invalid token type");
        }

        if (Role.STUDENT.equals(typeRole)) {
            User user = userRepository.findByEmail(verificationToken.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setEmailVerified(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        } else if (Role.VENDOR.equals(typeRole)) {
            Vendor vendor = vendorRepository.findByEmail(verificationToken.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            vendor.setEmailVerified(true);
            vendor.setUpdatedAt(LocalDateTime.now());
            vendorRepository.save(vendor);
        }

        verificationToken.setUsed(true);
        emailVerificationTokenRepository.save(verificationToken);
    }

    public void forgotPassword(ForgotPasswordDto forgotPasswordDto) {
        String email = forgotPasswordDto.getEmail();
        Role userType = forgotPasswordDto.getUserType();

        boolean userExists = false;
        if ("USER".equals(userType)) {
            userExists = userRepository.existsByEmail(email);
        } else if ("VENDOR".equals(userType)) {
            userExists = vendorRepository.existsByEmail(email);
        }

        if (!userExists) {
            throw new ResourceNotFoundException("User not found with email: " + email);
        }

        // Delete any existing reset tokens
        passwordResetTokenRepository.deleteByEmailAndUserType(email, userType);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setUserType(userType);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1));
        resetToken.setUsed(false);
        resetToken.setCreatedAt(LocalDateTime.now());

        passwordResetTokenRepository.save(resetToken);
        emailService.sendPasswordResetEmail(email, token, userType);
    }

    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(resetPasswordDto.getToken())
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));

        if (resetToken.isUsed()) {
            throw new BadRequestException("Token already used");
        }

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token has expired");
        }

        if (!resetToken.getUserType().equals(resetPasswordDto.getType())) {
            throw new BadRequestException("Invalid token type");
        }

        String encodedPassword = passwordEncoder.encode(resetPasswordDto.getNewPassword());

        if ("USER".equals(resetPasswordDto.getType())) {
            User user = userRepository.findByEmail(resetToken.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setPassword(encodedPassword);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        } else if ("VENDOR".equals(resetPasswordDto.getType())) {
            Vendor vendor = vendorRepository.findByEmail(resetToken.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            vendor.setPassword(encodedPassword);
            vendor.setUpdatedAt(LocalDateTime.now());
            vendorRepository.save(vendor);
        }

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
                throw new BadRequestException("Current password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
            return;
        }

        var vendorOpt = vendorRepository.findByEmail(email);
        if (vendorOpt.isPresent()) {
            Vendor vendor = vendorOpt.get();
            if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), vendor.getPassword())) {
                throw new BadRequestException("Current password is incorrect");
            }
            vendor.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            vendor.setUpdatedAt(LocalDateTime.now());
            vendorRepository.save(vendor);
            return;
        }

        throw new ResourceNotFoundException("User not found");
    }

    private void createAndSendVerificationToken(String email, Role userType) {
        emailVerificationTokenRepository.deleteByEmailAndUserType(email, userType);

        long expirationHours = Secrets.EMAIL_VERIFICATION_TOKEN_EXPIRATION / (1000 * 60 * 60);

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = new EmailVerificationToken();
        verificationToken.setToken(token);
        verificationToken.setEmail(email);
        verificationToken.setUserType(userType);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(expirationHours));
        verificationToken.setUsed(false);
        verificationToken.setCreatedAt(LocalDateTime.now());

        emailVerificationTokenRepository.save(verificationToken);
        emailService.sendVerificationEmail(email, token, userType);
    }

    public void resendVerificationEmail(String email, Role userType) {

        long expirationHours = Secrets.PASSWORD_RESET_TOKEN_EXPIRATION / (1000 * 60 * 60);

        boolean userExists = false;
        boolean emailVerified = false;

        if (userType == Role.STUDENT) {
            var user = userRepository.findByEmail(email);
            if (user.isPresent()) {
                userExists = true;
                emailVerified = user.get().isEmailVerified();
            }
        } else if (userType == Role.VENDOR) {
            var vendor = vendorRepository.findByEmail(email);
            if (vendor.isPresent()) {
                userExists = true;
                emailVerified = vendor.get().isEmailVerified();
            }
        }

        if (!userExists) {
            throw new ResourceNotFoundException("User not found");
        }

        if (emailVerified) {
            throw new BadRequestException("Email already verified");
        }

        createAndSendVerificationToken(email, userType);
    }

    public AuthResponseDto refreshToken(String refreshTokenValue) {
        String newAccessToken = refreshTokenService.generateNewAccessToken(refreshTokenValue);

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenValue);

        AuthResponseDto response = new AuthResponseDto();
        response.setToken(newAccessToken);
        response.setRefreshToken(refreshTokenValue); // Keep the same refresh token
        response.setUserId(refreshToken.getUserId());
        response.setEmail(refreshToken.getUserEmail());
        response.setMessage("Token refreshed successfully");

        if ("USER".equals(refreshToken.getUserType())) {
            User user = userRepository.findById(refreshToken.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            response.setName(user.getFullName());
            response.setRole(user.getRole());
        } else {
            Vendor vendor = vendorRepository.findById(refreshToken.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            response.setName(vendor.getVendorName());
            response.setRole(vendor.getRole());
        }

        return response;
    }

    public void logout(String refreshTokenValue) {
        refreshTokenService.revokeRefreshToken(refreshTokenValue);
    }
}

