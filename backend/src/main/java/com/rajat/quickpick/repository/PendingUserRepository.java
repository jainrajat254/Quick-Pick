package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.PendingUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingUserRepository extends MongoRepository<PendingUser, String> {

    Optional<PendingUser> findByEmail(String email);

    Optional<PendingUser> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    void deleteByEmail(String email);
}
