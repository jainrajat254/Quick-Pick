package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.PendingVendor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PendingVendorRepository extends MongoRepository<PendingVendor, String> {

    Optional<PendingVendor> findByEmail(String email);

    Optional<PendingVendor> findByPhone(String phone);

    Optional<PendingVendor> findByGstNumber(String gstNumber);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByGstNumber(String gstNumber);

    void deleteByEmail(String email);
}
