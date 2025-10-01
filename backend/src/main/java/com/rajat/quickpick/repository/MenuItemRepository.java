package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.MenuItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemRepository extends MongoRepository<MenuItem, String> {

    List<MenuItem> findByVendorId(String vendorId);

    List<MenuItem> findByVendorIdAndIsAvailable(String vendorId, boolean isAvailable);

    List<MenuItem> findByCategory(String category);

    List<MenuItem> findByVendorIdAndCategory(String vendorId, String category);

    List<MenuItem> findByIsVeg(boolean isVeg);

    List<MenuItem> findByVendorIdAndIsVeg(String vendorId, boolean isVeg);

    List<MenuItem> findByNameContainingIgnoreCase(String name);

    List<MenuItem> findByVendorIdAndNameContainingIgnoreCase(String vendorId, String name);

    List<MenuItem> findByPriceBetween(double minPrice, double maxPrice);

    List<MenuItem> findByVendorIdAndPriceBetween(String vendorId, double minPrice, double maxPrice);

    List<MenuItem> findByQuantityGreaterThan(int quantity);

    List<MenuItem> findByVendorIdAndQuantityGreaterThan(String vendorId, int quantity);

    boolean existsByVendorIdAndName(String vendorId, String name);
}