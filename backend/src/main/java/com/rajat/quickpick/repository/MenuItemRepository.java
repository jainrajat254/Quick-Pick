package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.MenuItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    Page<MenuItem> findByVendorId(String vendorId, Pageable pageable);

    List<MenuItem> findByVendorId(String vendorId);

    List<MenuItem> findByVendorIdAndIsAvailable(String vendorId, boolean isAvailable);

    List<MenuItem> findByCategory(String category);

    List<MenuItem> findByVendorIdAndCategory(String vendorId, String category);

    List<MenuItem> findByIsVeg(boolean isVeg);

    List<MenuItem> findByVendorIdAndIsVeg(String vendorId, boolean isVeg);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    List<MenuItem> findByVendorIdAndNameContainingIgnoreCase(String vendorId, String name);

    Page<MenuItem> findByVendorIdAndNameContainingIgnoreCase(String vendorId, String name, Pageable pageable);

    List<MenuItem> findByPriceBetween(double minPrice, double maxPrice);

    List<MenuItem> findByVendorIdAndPriceBetween(String vendorId, double minPrice, double maxPrice);

    List<MenuItem> findByQuantityGreaterThan(int quantity);

    List<MenuItem> findByVendorIdAndQuantityGreaterThan(String vendorId, int quantity);

    boolean existsByVendorIdAndName(String vendorId, String name);

    @Query("{ 'vendorId': { $in: ?0 }, 'name': { $regex: ?1, $options: 'i' }, 'isAvailable': true }")
    Page<MenuItem> findByVendorIdsAndNameContainingIgnoreCaseAndAvailable(List<String> vendorIds, String searchQuery, Pageable pageable);

    @Query("{ 'vendorId': { $in: ?0 } }")
    Page<MenuItem> findByVendorIdIn(List<String> vendorIds, Pageable pageable);
}