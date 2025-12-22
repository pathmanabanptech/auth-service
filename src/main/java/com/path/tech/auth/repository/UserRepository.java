package com.path.tech.auth.repository;

import com.path.tech.auth.model.UserProfile;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class UserRepository {
    Map<String, UUID> phoneUserIdMap = new HashMap<>();
    Map<UUID,UserProfile> userProfileMap = new HashMap<>();

    public UserProfile save(UserProfile profile) {
        UUID userID = UUID.randomUUID();
        profile.setId(userID);
        profile.setCreateDate(Instant.now());
        profile.setUpdatedDate(Instant.now());
        userProfileMap.put(userID, profile);
        phoneUserIdMap.put(profile.getPhoneNumber(),userID);
        return profile;
    }

    public UUID findUserIdByPhoneNumber(String phoneNumber) {
        return phoneUserIdMap.get(phoneNumber);
    }
}
