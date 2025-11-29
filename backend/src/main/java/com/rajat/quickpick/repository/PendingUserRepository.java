package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.PendingUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingUserRepository extends MongoRepository<PendingUser, String> {

    Optional<PendingUser> findByEmail(String email);

    Optional<PendingUser> findByPhone(String phone);

    Optional<PendingUser> findByStudentId(String studentId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByStudentId(String studentId);

    void deleteByEmail(String email);
}

