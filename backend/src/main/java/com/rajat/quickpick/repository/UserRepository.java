package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.User;
import com.rajat.quickpick.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);


    List<User> findByCollegeName(String collegeName);

    List<User> findByRole(Role role);

    List<User> findByCollegeNameAndRole(String collegeName, Role role);

    List<User> findByIsEmailVerified(boolean isEmailVerified);

    List<User> findByIsPhoneVerified(boolean isPhoneVerified);

    List<User> findBySuspended(boolean suspended);

    long countByCollegeName(String collegeName);

    Page<User> findByCollegeName(String collegeName, Pageable pageable);


}