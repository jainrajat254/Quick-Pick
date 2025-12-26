package com.rajat.quickpick.service;

import com.rajat.quickpick.dto.auth.*;
import com.rajat.quickpick.dto.user.RegisterUserDto;
import com.rajat.quickpick.dto.vendor.RegisterVendor;
import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.*;
import com.rajat.quickpick.enums.Role;
import com.rajat.quickpick.repository.*;
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
import java.util.Optional;

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
    private PendingUserRepository pendingUserRepository;
    @Autowired
    private PendingVendorRepository pendingVendorRepository;
    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired
    private DeviceTokenRepository deviceTokenRepository;
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
    private static final int EMAIL_OTP_MAX_ATTEMPTS = 5;
    private static final long PASSWORD_OTP_EXPIRATION_MINUTES = 10;
    private static final long PASSWORD_OTP_RESEND_COOLDOWN_SECONDS = 60;
    private static final int PASSWORD_OTP_MAX_ATTEMPTS = 5;

    public AuthResponseDto registerUser(RegisterUserDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BadRequestException("Email already registered and verified");
        }

        if (userRepository.existsByPhone(registrationDto.getPhone())) {
            throw new BadRequestException("Phone number already registered and verified");
        }

        Optional<PendingUser> existingPendingUser = pendingUserRepository.findByEmail(registrationDto.getEmail());

        PendingUser pendingUser;
        if (existingPendingUser.isPresent()) {
            pendingUser = existingPendingUser.get();
            pendingUser.setFullName(registrationDto.getFullName());
            pendingUser.setPhone(registrationDto.getPhone());
            pendingUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            pendingUser.setCollegeName(registrationDto.getCollegeName());
            pendingUser.setUpdatedAt(LocalDateTime.now());
        } else {
            pendingUser = new PendingUser();
            pendingUser.setFullName(registrationDto.getFullName());
            pendingUser.setPhone(registrationDto.getPhone());
            pendingUser.setEmail(registrationDto.getEmail());
            pendingUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            pendingUser.setCollegeName(registrationDto.getCollegeName());
            pendingUser.setRole(Role.STUDENT);
            pendingUser.setCreatedAt(LocalDateTime.now());
            pendingUser.setUpdatedAt(LocalDateTime.now());
        }

        String otp = generateNumericOtp(6);
        pendingUser.setOtp(passwordEncoder.encode(otp));
        pendingUser.setOtpCreatedAt(LocalDateTime.now());
        pendingUser.setOtpExpiresAt(LocalDateTime.now().plusMinutes(EMAIL_OTP_EXPIRATION_MINUTES));
        pendingUser.setOtpAttempts(0);

        PendingUser savedPendingUser = pendingUserRepository.save(pendingUser);

        emailService.sendEmailVerificationOtp(savedPendingUser.getEmail(), otp, Role.STUDENT, EMAIL_OTP_EXPIRATION_MINUTES);

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(savedPendingUser.getId());
        response.setEmail(savedPendingUser.getEmail());
        response.setName(savedPendingUser.getFullName());
        response.setRole(savedPendingUser.getRole());
        response.setMessage("Registration successful. We've emailed a verification code to verify your account.");

        return response;
    }

    public AuthResponseDto registerVendor(RegisterVendor registrationDto) {
        log.info("registerVendor called for email={}", registrationDto.getEmail());
        if (vendorRepository.existsByEmail(registrationDto.getEmail())) {
            throw new BadRequestException("Email already registered and verified");
        }

        if (vendorRepository.existsByPhone(registrationDto.getPhone())) {
            throw new BadRequestException("Phone number already registered and verified");
        }

        if (vendorRepository.existsByGstNumber(registrationDto.getGstNumber())) {
            throw new BadRequestException("GST number already registered and verified");
        }

        Optional<PendingVendor> existingPendingVendor = pendingVendorRepository.findByEmail(registrationDto.getEmail());

        PendingVendor pendingVendor;
        if (existingPendingVendor.isPresent()) {
            log.info("existing pending vendor found for email={}", registrationDto.getEmail());
            pendingVendor = existingPendingVendor.get();
            pendingVendor.setVendorName(registrationDto.getVendorName());
            pendingVendor.setStoreName(registrationDto.getStoreName());
            pendingVendor.setPhone(registrationDto.getPhone());
            pendingVendor.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            pendingVendor.setAddress(registrationDto.getAddress());
            pendingVendor.setCollegeName(registrationDto.getCollegeName());
            pendingVendor.setVendorDescription(registrationDto.getVendorDescription());
            List<String> categories = registrationDto.getFoodCategories();
            if (categories == null || categories.isEmpty()) {
                categories = DEFAULT_CATEGORIES;
            }
            pendingVendor.setFoodCategories(categories);
            pendingVendor.setGstNumber(registrationDto.getGstNumber());
            pendingVendor.setLicenseNumber(registrationDto.getLicenseNumber());
            pendingVendor.setUpdatedAt(LocalDateTime.now());
        } else {
            log.info("creating new pending vendor for email={}", registrationDto.getEmail());
            pendingVendor = new PendingVendor();
            pendingVendor.setVendorName(registrationDto.getVendorName());
            pendingVendor.setStoreName(registrationDto.getStoreName());
            pendingVendor.setEmail(registrationDto.getEmail());
            pendingVendor.setPhone(registrationDto.getPhone());
            pendingVendor.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
            pendingVendor.setAddress(registrationDto.getAddress());
            pendingVendor.setCollegeName(registrationDto.getCollegeName());
            pendingVendor.setVendorDescription(registrationDto.getVendorDescription());
            List<String> categories = registrationDto.getFoodCategories();
            if (categories == null || categories.isEmpty()) {
                categories = DEFAULT_CATEGORIES;
            }
            pendingVendor.setFoodCategories(categories);
            pendingVendor.setGstNumber(registrationDto.getGstNumber());
            pendingVendor.setLicenseNumber(registrationDto.getLicenseNumber());
            pendingVendor.setRole(Role.VENDOR);
            pendingVendor.setCreatedAt(LocalDateTime.now());
            pendingVendor.setUpdatedAt(LocalDateTime.now());
        }

        String otp = generateNumericOtp(6);
        pendingVendor.setOtp(passwordEncoder.encode(otp));
        pendingVendor.setOtpCreatedAt(LocalDateTime.now());
        pendingVendor.setOtpExpiresAt(LocalDateTime.now().plusMinutes(EMAIL_OTP_EXPIRATION_MINUTES));
        pendingVendor.setOtpAttempts(0);

        PendingVendor savedPendingVendor = pendingVendorRepository.save(pendingVendor);
        log.info("pending vendor saved with id={}", savedPendingVendor.getId());

        if (!vendorRepository.existsByEmail(savedPendingVendor.getEmail())) {
            Vendor vendor = new Vendor();
            vendor.setVendorName(savedPendingVendor.getVendorName());
            vendor.setStoreName(savedPendingVendor.getStoreName());
            vendor.setEmail(savedPendingVendor.getEmail());
            vendor.setPhone(savedPendingVendor.getPhone());
            vendor.setPassword(savedPendingVendor.getPassword());
            vendor.setAddress(savedPendingVendor.getAddress());
            vendor.setCollegeName(savedPendingVendor.getCollegeName());
            vendor.setVendorDescription(savedPendingVendor.getVendorDescription());
            vendor.setFoodCategories(savedPendingVendor.getFoodCategories());
            vendor.setGstNumber(savedPendingVendor.getGstNumber());
            vendor.setLicenseNumber(savedPendingVendor.getLicenseNumber());
            vendor.setRole(Role.VENDOR);
            vendor.setEmailVerified(false);
            vendor.setPhoneVerified(false);
            vendor.setVerificationStatus(com.rajat.quickpick.enums.VendorVerificationStatus.PENDING);
            vendor.setCreatedAt(LocalDateTime.now());
            vendor.setUpdatedAt(LocalDateTime.now());
            Vendor savedVendor = vendorRepository.save(vendor);
            log.info("vendor record created with id={} and PENDING status", savedVendor.getId());

            emailService.sendVendorRegistrationNotificationToAdmin(
                    savedVendor.getVendorName(),
                    savedVendor.getEmail(),
                    savedVendor.getStoreName(),
                    savedVendor.getCollegeName()
            );
        }

        emailService.sendEmailVerificationOtp(savedPendingVendor.getEmail(), otp, Role.VENDOR, EMAIL_OTP_EXPIRATION_MINUTES);

        AuthResponseDto response = new AuthResponseDto();
        response.setUserId(savedPendingVendor.getId());
        response.setEmail(savedPendingVendor.getEmail());
        response.setName(savedPendingVendor.getVendorName());
        response.setRole(savedPendingVendor.getRole());
        response.setMessage("Registration successful. We've emailed a verification code to verify your account.");

        return response;
    }

    public AuthResponseDto login(String email, String password, String userType) {
        log.info("login attempt");
        log.info("email {}", email);
        log.info("usertype {}", userType);
        log.info("password length {}", password.length());

        try {
            var tempUserOpt = userRepository.findByEmail(email);
            if (tempUserOpt.isPresent()) {
                User u = tempUserOpt.get();
                log.info("user found in db id {}", u.getId());
                log.info("email verified {}", u.isEmailVerified());
                log.info("suspended {}", u.isSuspended());
                log.info("password hash in db {}", u.getPassword());
                log.info("testing password match {}", passwordEncoder.matches(password, u.getPassword()));
            } else {
                log.info("user not found in users collection");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            log.info("authentication successful");

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
                        .expiresIn(Secrets.JWT_EXPIRATION / 1000)
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
                        .expiresIn(Secrets.JWT_EXPIRATION / 1000)
                        .tokenType("Bearer")
                        .build();

                response.setTokens(tokens);
                response.setUserId(vendor.getId());
                response.setEmail(vendor.getEmail());
                response.setName(vendor.getVendorName());
                response.setRole(vendor.getRole());
                response.setMessage("Login successful");

                log.info("login successful for email {}", email);
                return response;
            }

            throw new BadRequestException("Invalid credentials or user type mismatch");

        } catch (Exception e) {
            log.info("login failed for email {} error {}", email, e.getMessage());
            throw new BadRequestException("Invalid credentials");

        }
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

        if (userType == Role.STUDENT) {
            PendingUser pendingUser = pendingUserRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Invalid or expired code"));

            if (pendingUser.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Code has expired");
            }

            int attempts = pendingUser.getOtpAttempts() == null ? 0 : pendingUser.getOtpAttempts();
            if (attempts >= EMAIL_OTP_MAX_ATTEMPTS) {
                throw new BadRequestException("Too many attempts. Please request a new code");
            }

            if (!passwordEncoder.matches(otp, pendingUser.getOtp())) {
                pendingUser.setOtpAttempts(attempts + 1);
                pendingUserRepository.save(pendingUser);
                throw new BadRequestException("Incorrect code");
            }

            User user = new User();
            user.setFullName(pendingUser.getFullName());
            user.setPhone(pendingUser.getPhone());
            user.setEmail(pendingUser.getEmail());
            user.setPassword(pendingUser.getPassword());
            user.setCollegeName(pendingUser.getCollegeName());
            user.setRole(Role.STUDENT);
            user.setEmailVerified(true);
            user.setPhoneVerified(false);
            user.setCreatedAt(pendingUser.getCreatedAt());
            user.setUpdatedAt(LocalDateTime.now());

            userRepository.save(user);
            pendingUserRepository.deleteByEmail(email);

        } else if (userType == Role.VENDOR) {
            PendingVendor pendingVendor = pendingVendorRepository.findByEmail(email)
                    .orElseThrow(() -> new BadRequestException("Invalid or expired code"));

            if (pendingVendor.getOtpExpiresAt().isBefore(LocalDateTime.now())) {
                throw new BadRequestException("Code has expired");
            }

            int attempts = pendingVendor.getOtpAttempts() == null ? 0 : pendingVendor.getOtpAttempts();
            if (attempts >= EMAIL_OTP_MAX_ATTEMPTS) {
                throw new BadRequestException("Too many attempts. Please request a new code");
            }

            if (!passwordEncoder.matches(otp, pendingVendor.getOtp())) {
                pendingVendor.setOtpAttempts(attempts + 1);
                pendingVendorRepository.save(pendingVendor);
                throw new BadRequestException("Incorrect code");
            }

            var existingVendorOpt = vendorRepository.findByEmail(email);
            Vendor notifVendor = null;
            if (existingVendorOpt.isPresent()) {
                Vendor existingVendor = existingVendorOpt.get();
                existingVendor.setEmailVerified(true);
                existingVendor.setUpdatedAt(LocalDateTime.now());
                vendorRepository.save(existingVendor);
                notifVendor = existingVendor;
                log.info("existing vendor updated emailVerified=true for email={}", email);
            } else {
                Vendor vendor = new Vendor();
                vendor.setVendorName(pendingVendor.getVendorName());
                vendor.setStoreName(pendingVendor.getStoreName());
                vendor.setEmail(pendingVendor.getEmail());
                vendor.setPhone(pendingVendor.getPhone());
                vendor.setPassword(pendingVendor.getPassword());
                vendor.setAddress(pendingVendor.getAddress());
                vendor.setCollegeName(pendingVendor.getCollegeName());
                vendor.setVendorDescription(pendingVendor.getVendorDescription());
                vendor.setFoodCategories(pendingVendor.getFoodCategories());
                vendor.setGstNumber(pendingVendor.getGstNumber());
                vendor.setLicenseNumber(pendingVendor.getLicenseNumber());
                vendor.setRole(Role.VENDOR);
                vendor.setEmailVerified(true);
                vendor.setPhoneVerified(false);
                vendor.setCreatedAt(pendingVendor.getCreatedAt());
                vendor.setUpdatedAt(LocalDateTime.now());

                Vendor savedVendor = vendorRepository.save(vendor);
                notifVendor = savedVendor;
                log.info("vendor created on verify for email={}", email);
            }
            pendingVendorRepository.deleteByEmail(email);

            if (notifVendor != null) {
                emailService.sendVendorRegistrationNotificationToAdmin(
                        notifVendor.getVendorName(),
                        notifVendor.getEmail(),
                        notifVendor.getStoreName(),
                        notifVendor.getCollegeName()
                );
            }
        }
    }

    public void resendVerificationEmail(String email, Role userType) {
        if (userType == Role.STUDENT) {
            PendingUser pendingUser = pendingUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            String otp = generateNumericOtp(6);
            pendingUser.setOtp(passwordEncoder.encode(otp));
            pendingUser.setOtpCreatedAt(LocalDateTime.now());
            pendingUser.setOtpExpiresAt(LocalDateTime.now().plusMinutes(EMAIL_OTP_EXPIRATION_MINUTES));
            pendingUser.setOtpAttempts(0);
            pendingUser.setUpdatedAt(LocalDateTime.now());

            pendingUserRepository.save(pendingUser);
            emailService.sendEmailVerificationOtp(email, otp, Role.STUDENT, EMAIL_OTP_EXPIRATION_MINUTES);

        } else if (userType == Role.VENDOR) {
            PendingVendor pendingVendor = pendingVendorRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            String otp = generateNumericOtp(6);
            pendingVendor.setOtp(passwordEncoder.encode(otp));
            pendingVendor.setOtpCreatedAt(LocalDateTime.now());
            pendingVendor.setOtpExpiresAt(LocalDateTime.now().plusMinutes(EMAIL_OTP_EXPIRATION_MINUTES));
            pendingVendor.setOtpAttempts(0);
            pendingVendor.setUpdatedAt(LocalDateTime.now());

            pendingVendorRepository.save(pendingVendor);
            emailService.sendEmailVerificationOtp(email, otp, Role.VENDOR, EMAIL_OTP_EXPIRATION_MINUTES);
        }
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

    public void logout(String userId, String refreshTokenValue) {
        log.info("logout request for user {}", userId);

        if (refreshTokenValue != null && !refreshTokenValue.isEmpty()) {
            refreshTokenService.revokeRefreshToken(refreshTokenValue);
            log.info("revoked refresh token for user {}", userId);
        }

        deviceTokenRepository.deleteAllByUserId(userId);
        log.info("cleared all fcm device tokens for user {}", userId);

        log.info("logout successful for user {}", userId);
    }

    private String generateNumericOtp(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
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

    public void sendEmailOtp(String email, Role userType) {
        final int MAX_SENDS = 3;
        final long WINDOW_SECONDS = 60;
        final int MAX_WRONG_ATTEMPTS = 5;

        if (userType == Role.STUDENT) {
            PendingUser pendingUser = pendingUserRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Integer sends = pendingUser.getOtpSendCount() == null ? 0 : pendingUser.getOtpSendCount();
            LocalDateTime lastSent = pendingUser.getOtpLastSentAt();

            if (lastSent != null && lastSent.plusSeconds(WINDOW_SECONDS).isAfter(LocalDateTime.now())) {
                if (sends >= MAX_SENDS) {
                    throw new BadRequestException("OTP resend limit reached. Please try again later.");
                }
            } else {
                sends = 0;
            }

            Integer attempts = pendingUser.getOtpAttempts() == null ? 0 : pendingUser.getOtpAttempts();
            if (attempts >= MAX_WRONG_ATTEMPTS) {
                throw new BadRequestException("Too many incorrect OTP attempts. Please restart registration process.");
            }

            String otp = generateNumericOtp(6);
            pendingUser.setOtp(passwordEncoder.encode(otp));
            pendingUser.setOtpCreatedAt(LocalDateTime.now());
            pendingUser.setOtpExpiresAt(LocalDateTime.now().plusMinutes(EMAIL_OTP_EXPIRATION_MINUTES));
            pendingUser.setOtpAttempts(0);
            pendingUser.setUpdatedAt(LocalDateTime.now());

            pendingUser.setOtpSendCount(sends + 1);
            pendingUser.setOtpLastSentAt(LocalDateTime.now());

            pendingUserRepository.save(pendingUser);
            emailService.sendEmailVerificationOtp(email, otp, Role.STUDENT, EMAIL_OTP_EXPIRATION_MINUTES);

        } else if (userType == Role.VENDOR) {
            PendingVendor pendingVendor = pendingVendorRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            Integer sends = pendingVendor.getOtpSendCount() == null ? 0 : pendingVendor.getOtpSendCount();
            LocalDateTime lastSent = pendingVendor.getOtpLastSentAt();

            if (lastSent != null && lastSent.plusSeconds(WINDOW_SECONDS).isAfter(LocalDateTime.now())) {
                if (sends >= MAX_SENDS) {
                    throw new BadRequestException("OTP resend limit reached. Please try again later.");
                }
            } else {
                sends = 0;
            }

            Integer attempts = pendingVendor.getOtpAttempts() == null ? 0 : pendingVendor.getOtpAttempts();
            if (attempts >= MAX_WRONG_ATTEMPTS) {
                throw new BadRequestException("Too many incorrect OTP attempts. Please restart registration process.");
            }

            String otp = generateNumericOtp(6);
            pendingVendor.setOtp(passwordEncoder.encode(otp));
            pendingVendor.setOtpCreatedAt(LocalDateTime.now());
            pendingVendor.setOtpExpiresAt(LocalDateTime.now().plusMinutes(EMAIL_OTP_EXPIRATION_MINUTES));
            pendingVendor.setOtpAttempts(0);
            pendingVendor.setUpdatedAt(LocalDateTime.now());

            pendingVendor.setOtpSendCount(sends + 1);
            pendingVendor.setOtpLastSentAt(LocalDateTime.now());

            pendingVendorRepository.save(pendingVendor);
            emailService.sendEmailVerificationOtp(email, otp, Role.VENDOR, EMAIL_OTP_EXPIRATION_MINUTES);
        } else {
            throw new BadRequestException("Unsupported user type");
        }
    }
}
