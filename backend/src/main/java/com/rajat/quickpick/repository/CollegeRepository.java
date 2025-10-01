package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.College;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollegeRepository extends MongoRepository<College, String> {

    Optional<College> findByName(String name);

    boolean existsByName(String name);

    List<College> findByNameContainingIgnoreCase(String name);

    List<College> findByCity(String city);

    List<College> findByState(String state);

    List<College> findByCityAndState(String city, String state);

    List<College> findByAddressContainingIgnoreCase(String address);
}