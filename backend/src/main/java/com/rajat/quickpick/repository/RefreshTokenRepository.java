package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.RefreshToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends MongoRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUserIdAndRevokedFalse(String userId);

    void deleteByUserId(String userId);

    void deleteByExpiryDateBefore(LocalDateTime dateTime);

    List<RefreshToken> findByExpiryDateBeforeAndRevokedFalse(LocalDateTime dateTime);
}
