package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.PasswordResetToken;
import com.rajat.quickpick.model.enums.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {

    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByEmailAndUserType(String email, String userType);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);

    void deleteByEmailAndUserType(String email, Role userType);
}