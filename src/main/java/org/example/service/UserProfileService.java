package org.example.service;

import org.example.dto.UserProfile;
import org.example.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository profileRepository;

    public UserProfile getProfile(String userId) {
        return profileRepository.findById(userId);
    }

    public UserProfile updateProfile(UserProfile profile) {
        UserProfile existingProfile = profileRepository.findById(profile.getUserId());
        if (existingProfile != null) {
            profile.setCreatedAt(existingProfile.getCreatedAt());
        } else {
            profile.setCreatedAt(System.currentTimeMillis());
        }
        profile.setUpdatedAt(System.currentTimeMillis());
        return profileRepository.save(profile);
    }
}
