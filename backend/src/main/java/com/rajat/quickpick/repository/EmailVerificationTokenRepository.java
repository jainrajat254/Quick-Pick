package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.EmailVerificationToken;
import com.rajat.quickpick.model.enums.Role;
import org.apache.tomcat.util.descriptor.web.SecurityRoleRef;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailVerificationTokenRepository extends MongoRepository<EmailVerificationToken, String> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByEmailAndUserType(String email, String userType);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);

    void deleteByEmailAndUserType(String email, Role userType);
}