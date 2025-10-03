package com.rajat.quickpick.repository;

import com.rajat.quickpick.enums.VendorVerificationStatus;
import com.rajat.quickpick.model.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends MongoRepository<Vendor, String> {

    Optional<Vendor> findByEmail(String email);

    Optional<Vendor> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    boolean existsByGstNumber(String gstNumber);

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByFoodLicenseNumber(String foodLicenseNumber);

    List<Vendor> findByCollegeName(String collegeName);

    List<Vendor> findByFoodCategoriesContaining(String category);

    List<Vendor> findByStoreNameContainingIgnoreCase(String storeName);

    List<Vendor> findByVendorNameContainingIgnoreCase(String vendorName);

    List<Vendor> findByCollegeNameAndFoodCategoriesContaining(String collegeName, String category);

    List<Vendor> findByIsEmailVerified(boolean isEmailVerified);

    List<Vendor> findByIsPhoneVerified(boolean isPhoneVerified);

    List<Vendor> findByVerificationStatus(VendorVerificationStatus status);

    Page<Vendor> findByVerificationStatus(VendorVerificationStatus status, Pageable pageable);

    long countByVerificationStatus(VendorVerificationStatus status);

    long countByCollegeName(String collegeName);


}