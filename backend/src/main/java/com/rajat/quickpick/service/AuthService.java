package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.auth.*;
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

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

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

    public static final List<String> DEFAULT_CATEGORIES = List.of(
            "Beverages", "Snacks", "Main Course", "Desserts", "Fast Food",
            "Indian", "Chinese", "South Indian", "North Indian", "Continental",
            "Sandwiches", "Pizza", "Burgers", "Rolls", "Salads", "Juices",
            "Tea/Coffee", "Ice Cream", "Sweets", "Breakfast"
    );

    private static final long EMAIL_OTP_EXPIRATION_MINUTES = 10;
    private static final long EMAIL_OTP_RESEND_COOLDOWN_SECONDS = 60;
    private static final int EMAIL_OTP_MAX_ATTEMPTS = 5;
    private static final long PASSWORD_OTP_EXPIRATION_MINUTES = 10;
    private static final long PASSWORD_OTP_RESEND_COOLDOWN_SECONDS = 60;
    private static final int PASSWORD_OTP_MAX_ATTEMPTS = 5;

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

        User user = new User();
        user.setFullName(registrationDto.getFullName());
        user.setGender(registrationDto.getGender());
        user.setPhone(registrationDto.getPhone());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setCollegeName(registrationDto.getCollegeName());
        user.setStudentId(registrationDto.getStudentId());
        user.setRole(Role.STUDENT);
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        createAndSendEmailOtp(savedUser.getEmail(), Role.STUDENT);

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(savedUser.getId());
        response.setEmail(savedUser.getEmail());
        response.setName(savedUser.getFullName());
        response.setRole(savedUser.getRole());
        response.setMessage("Registration successful. We've emailed a verification code to verify your account.");

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
        List<String> categories = registrationDto.getFoodCategories();
        if (categories == null || categories.isEmpty()) {
            categories = DEFAULT_CATEGORIES;
        }
        vendor.setFoodCategories(categories);
        vendor.setGstNumber(registrationDto.getGstNumber());
        vendor.setLicenseNumber(registrationDto.getLicenseNumber());
        vendor.setFoodLicenseNumber(registrationDto.getFoodLicenseNumber());
        vendor.setRole(Role.VENDOR);
        vendor.setEmailVerified(false);
        vendor.setPhoneVerified(false);
        vendor.setCreatedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());

        Vendor savedVendor = vendorRepository.save(vendor);

        createAndSendEmailOtp(savedVendor.getEmail(), Role.VENDOR);

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(savedVendor.getId());
        response.setEmail(savedVendor.getEmail());
        response.setName(savedVendor.getVendorName());
        response.setRole(savedVendor.getRole());
        response.setMessage("Registration successful. We've emailed a verification code to verify your account.");

        return response;
    }

    public AuthResponseDto login(String email, String password, String userType) {
        final Logger logger = Logger.getLogger(AuthService.class.getName());
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            var userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                User user = userOpt.get();

                if (!user.isEmailVerified()) {
                    throw new BadRequestException("Please verify your email first");
                }

                String token = jwtUtil.generateToken(userDetails, user.getId(), user.getRole().name());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId(), user.getEmail(), user.getRole().name());

                AuthResponseDto response = new AuthResponseDto();

                TokensDto tokens = TokensDto.builder()
                        .accessToken(token)
                        .refreshToken(refreshToken.getToken())
                        .expiresIn(Secrets.JWT_EXPIRATION / 1000) // Convert milliseconds to seconds
                        .tokenType("Bearer")
                        .build();

                response.setTokens(tokens);
                response.setUserId(user.getId());
                response.setEmail(user.getEmail());
                response.setName(user.getFullName());
                response.setRole(user.getRole());
                response.setMessage("Login successful");

                return response;
            }

            var vendorOpt = vendorRepository.findByEmail(email);
            if (vendorOpt.isPresent() && "VENDOR".equals(userType)) {
                Vendor vendor = vendorOpt.get();

                if (!vendor.isEmailVerified()) {
                    throw new BadRequestException("Please verify your email first");
                }

                String token = jwtUtil.generateToken(userDetails, vendor.getId(), vendor.getRole().name());
                RefreshToken refreshToken = refreshTokenService.createRefreshToken(vendor.getId(), vendor.getEmail(), vendor.getRole().name());

                AuthResponseDto response = new AuthResponseDto();

                TokensDto tokens = TokensDto.builder()
                        .accessToken(token)
                        .refreshToken(refreshToken.getToken())
                        .expiresIn(Secrets.JWT_EXPIRATION / 1000) // Convert milliseconds to seconds
                        .tokenType("Bearer")
                        .build();

                response.setTokens(tokens);
                response.setUserId(vendor.getId());
                response.setEmail(vendor.getEmail());
                response.setName(vendor.getVendorName());
                response.setRole(vendor.getRole());
                response.setMessage("Login successful");

                logger.info("Login successful for email: " + email);
                return response;
            }

            throw new BadRequestException("Invalid credentials or user type mismatch");

        } catch (Exception e) {
            logger.info("Login failed for email: " + email + " - " + e.getMessage());
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
        PasswordOtpRequestDto dto = new PasswordOtpRequestDto();
        dto.setEmail(forgotPasswordDto.getEmail());
        dto.setUserType(forgotPasswordDto.getUserType());
        sendPasswordResetOtp(dto);
    }

    public void sendPasswordResetOtp(PasswordOtpRequestDto dto) {
        String email = dto.getEmail();
        Role userType = dto.getUserType();
        boolean exists = (userType == Role.STUDENT) ? userRepository.existsByEmail(email) : vendorRepository.existsByEmail(email);
        if (!exists) {
            return;
        }
        var existingOpt = passwordResetTokenRepository.findByEmailAndUserType(email, userType.name());
        if (existingOpt.isPresent()) {
            PasswordResetToken existing = existingOpt.get();
            if (existing.getLastSentAt() != null && existing.getLastSentAt().plusSeconds(PASSWORD_OTP_RESEND_COOLDOWN_SECONDS).isAfter(LocalDateTime.now())) {
                throw new BadRequestException("Please wait before requesting another password reset code");
            }
            passwordResetTokenRepository.deleteByEmailAndUserType(email, userType);
        }
        String otp = generateNumericOtp(6);
        String hash = passwordEncoder.encode(otp);
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setUserType(userType);
        token.setToken(hash);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(PASSWORD_OTP_EXPIRATION_MINUTES));
        token.setUsed(false);
        token.setCreatedAt(LocalDateTime.now());
        token.setAttempts(0);
        token.setLastSentAt(LocalDateTime.now());
        passwordResetTokenRepository.save(token);
        emailService.sendPasswordResetOtp(email, otp, PASSWORD_OTP_EXPIRATION_MINUTES);
    }

    public void resetPasswordWithOtp(ResetPasswordOtpDto dto) {
        String email = dto.getEmail();
        Role userType = dto.getUserType();
        String otp = dto.getOtp();
        PasswordResetToken token = passwordResetTokenRepository.findByEmailAndUserType(email, userType.name())
                .orElseThrow(() -> new BadRequestException("Invalid or expired code"));
        if (token.isUsed()) {
            throw new BadRequestException("Code already used");
        }
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Code has expired");
        }
        int attempts = token.getAttempts() == null ? 0 : token.getAttempts();
        if (attempts >= PASSWORD_OTP_MAX_ATTEMPTS) {
            throw new BadRequestException("Too many attempts. Request a new code");
        }
        if (!passwordEncoder.matches(otp, token.getToken())) {
            token.setAttempts(attempts + 1);
            passwordResetTokenRepository.save(token);
            throw new BadRequestException("Incorrect code");
        }
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());
        if (userType == Role.STUDENT) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setPassword(encodedPassword);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        } else if (userType == Role.VENDOR) {
            Vendor vendor = vendorRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            vendor.setPassword(encodedPassword);
            vendor.setUpdatedAt(LocalDateTime.now());
            vendorRepository.save(vendor);
        }
        token.setUsed(true);
        passwordResetTokenRepository.save(token);
    }

    public void verifyEmailOtp(EmailOtpVerifyDto dto) {
        String email = dto.getEmail();
        Role userType = dto.getUserType();
        String otp = dto.getOtp();

        EmailVerificationToken token = emailVerificationTokenRepository.findByEmailAndUserType(email, userType.name())
                .orElseThrow(() -> new BadRequestException("Invalid or expired code"));

        if (token.isUsed()) {
            throw new BadRequestException("Code already used");
        }
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Code has expired");
        }
        int attempts = token.getAttempts() == null ? 0 : token.getAttempts();
        if (attempts >= EMAIL_OTP_MAX_ATTEMPTS) {
            throw new BadRequestException("Too many attempts. Please request a new code");
        }

        if (!passwordEncoder.matches(otp, token.getToken())) {
            token.setAttempts(attempts + 1);
            emailVerificationTokenRepository.save(token);
            throw new BadRequestException("Incorrect code");
        }

        if (userType == Role.STUDENT) {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setEmailVerified(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        } else if (userType == Role.VENDOR) {
            Vendor vendor = vendorRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            vendor.setEmailVerified(true);
            vendor.setUpdatedAt(LocalDateTime.now());
            vendorRepository.save(vendor);
        }

        token.setUsed(true);
        emailVerificationTokenRepository.save(token);
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

        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshTokenValue);

        AuthResponseDto response = new AuthResponseDto();

        TokensDto tokens = TokensDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresIn(Secrets.JWT_EXPIRATION / 1000)
                .tokenType("Bearer")
                .build();

        response.setTokens(tokens);
        response.setUserId(newRefreshToken.getUserId());
        response.setEmail(newRefreshToken.getUserEmail());
        response.setMessage("Token refreshed successfully");

        Role userRole;
        try {
            userRole = Role.valueOf(newRefreshToken.getUserType());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid user type in refresh token");
        }

        if (Role.STUDENT.equals(userRole)) {
            User user = userRepository.findById(newRefreshToken.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            response.setName(user.getFullName());
            response.setRole(user.getRole());
        } else if (Role.VENDOR.equals(userRole)) {
            Vendor vendor = vendorRepository.findById(newRefreshToken.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            response.setName(vendor.getVendorName());
            response.setRole(vendor.getRole());
        } else {
            throw new BadRequestException("Unsupported role type");
        }

        return response;
    }

    public void logout(String refreshTokenValue) {
        refreshTokenService.revokeRefreshToken(refreshTokenValue);
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

    private String generateNumericOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private void createAndSendEmailOtp(String email, Role userType) {
        emailVerificationTokenRepository.deleteByEmailAndUserType(email, userType);

        String otp = generateNumericOtp(6);
        String otpHash = passwordEncoder.encode(otp);

        EmailVerificationToken token = new EmailVerificationToken();
        token.setEmail(email);
        token.setUserType(userType);
        token.setToken(otpHash);
        token.setExpiryDate(LocalDateTime.now().plusMinutes(EMAIL_OTP_EXPIRATION_MINUTES));
        token.setUsed(false);
        token.setCreatedAt(LocalDateTime.now());
        token.setAttempts(0);
        token.setLastSentAt(LocalDateTime.now());

        emailVerificationTokenRepository.save(token);

        emailService.sendEmailVerificationOtp(email, otp, userType, EMAIL_OTP_EXPIRATION_MINUTES);
    }

    public void validateResetToken(String token, String type) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid reset token"));
        if (resetToken.isUsed()) {
            throw new BadRequestException("Token already used");
        }
        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token has expired");
        }
        Role role;
        try { role = Role.valueOf(type); } catch (IllegalArgumentException e) { throw new BadRequestException("Invalid user type"); }
        if (!resetToken.getUserType().equals(role)) {
            throw new BadRequestException("Invalid token type");
        }
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
        Role role;
        try { role = Role.valueOf(resetPasswordDto.getType()); } catch (IllegalArgumentException e) { throw new BadRequestException("Invalid user type"); }
        if (!resetToken.getUserType().equals(role)) {
            throw new BadRequestException("Invalid token type");
        }
        String encoded = passwordEncoder.encode(resetPasswordDto.getNewPassword());
        if (role == Role.STUDENT) {
            User user = userRepository.findByEmail(resetToken.getEmail()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            user.setPassword(encoded); user.setUpdatedAt(LocalDateTime.now()); userRepository.save(user);
        } else if (role == Role.VENDOR) {
            Vendor vendor = vendorRepository.findByEmail(resetToken.getEmail()).orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            vendor.setPassword(encoded); vendor.setUpdatedAt(LocalDateTime.now()); vendorRepository.save(vendor);
        } else {
            throw new BadRequestException("Unsupported role type");
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

    public void sendEmailOtp(EmailOtpRequestDto dto) {
        String email = dto.getEmail();
        Role userType = dto.getUserType();
        boolean exists = (userType == Role.STUDENT) ? userRepository.existsByEmail(email) : vendorRepository.existsByEmail(email);
        if (!exists) {
            return;
        }
        var existingOpt = emailVerificationTokenRepository.findByEmailAndUserType(email, userType.name());
        if (existingOpt.isPresent()) {
            EmailVerificationToken existing = existingOpt.get();
            if (existing.getLastSentAt() != null && existing.getLastSentAt().plusSeconds(EMAIL_OTP_RESEND_COOLDOWN_SECONDS).isAfter(LocalDateTime.now())) {
                throw new BadRequestException("Please wait before requesting another code");
            }
            emailVerificationTokenRepository.deleteByEmailAndUserType(email, userType);
        }
        createAndSendEmailOtp(email, userType);
    }
}
