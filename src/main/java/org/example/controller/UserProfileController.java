package org.example.controller;

import org.example.dto.UserProfile;
import org.example.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"https://podiweda.com", "https://www.podiweda.com"}, allowCredentials = "true")
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private UserProfileService profileService;

    @GetMapping
    public ResponseEntity<UserProfile> getProfile(Authentication authentication) {
        log.info("Fetching user profile");
        String userId = getUserIdFromAuth(authentication);
        UserProfile profile = profileService.getProfile(userId);
        
        if (profile == null) {
            log.info("Profile not found, returning empty profile");
            profile = new UserProfile();
            profile.setUserId(userId);
        }
        
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserProfile> updateProfile(@RequestBody UserProfile profile, Authentication authentication) {
        log.info("Updating user profile");
        String userId = getUserIdFromAuth(authentication);
        profile.setUserId(userId);
        UserProfile updatedProfile = profileService.updateProfile(profile);
        log.info("Profile updated successfully");
        return ResponseEntity.ok(updatedProfile);
    }

    private String getUserIdFromAuth(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getSubject();
    }
}
