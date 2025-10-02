package com.rajat.quickpick.service;

import com.rajat.quickpick.exception.BadRequestException;
import com.rajat.quickpick.exception.ResourceNotFoundException;
import com.rajat.quickpick.model.RefreshToken;
import com.rajat.quickpick.model.User;
import com.rajat.quickpick.model.Vendor;
import com.rajat.quickpick.repository.RefreshTokenRepository;
import com.rajat.quickpick.repository.UserRepository;
import com.rajat.quickpick.repository.VendorRepository;
import com.rajat.quickpick.security.JwtUtil;
import com.rajat.quickpick.utils.Secrets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VendorRepository vendorRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    public RefreshToken createRefreshToken(String userId, String userEmail, String userType) {
        // Revoke existing refresh tokens for this user
        revokeUserRefreshTokens(userId);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserId(userId);
        refreshToken.setUserEmail(userEmail);
        refreshToken.setUserType(userType);
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(Secrets.JWT_REFRESH_EXPIRATION / 1000));
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setRevoked(false);

        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Refresh token not found"));
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new BadRequestException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    public void revokeRefreshToken(String tokenValue) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(tokenValue);
        if (refreshToken.isPresent()) {
            RefreshToken token = refreshToken.get();
            token.setRevoked(true);
            refreshTokenRepository.save(token);
        }
    }

    public void revokeUserRefreshTokens(String userId) {
        refreshTokenRepository.findByUserIdAndRevokedFalse(userId)
                .forEach(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    public String generateNewAccessToken(String refreshTokenValue) {
        RefreshToken refreshToken = findByToken(refreshTokenValue);

        if (refreshToken.isRevoked()) {
            throw new BadRequestException("Refresh token is revoked");
        }

        verifyExpiration(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(refreshToken.getUserEmail());

        String userId = refreshToken.getUserId();
        String role;

        if ("USER".equals(refreshToken.getUserType())) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            role = user.getRole().name();
        } else {
            Vendor vendor = vendorRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
            role = vendor.getRole().name();
        }

        return jwtUtil.generateToken(userDetails, userId, role);
    }

    public void deleteExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(LocalDateTime.now());
    }
}
