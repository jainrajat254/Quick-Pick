package com.rajat.quickpick.repository;

import com.rajat.quickpick.model.DeviceToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceTokenRepository extends MongoRepository<DeviceToken, String> {

    List<DeviceToken> findByUserIdAndActive(String userId, boolean active);

    Optional<DeviceToken> findByUserIdAndDeviceId(String userId, String deviceId);

    List<DeviceToken> findByUserId(String userId);

    void deleteByUserId(String userId);

    void deleteAllByUserId(String userId);

    Optional<DeviceToken> findByFcmToken(String fcmToken);
}