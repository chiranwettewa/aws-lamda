package org.example.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SecuredController {

    @GetMapping("/user/data")
    @PreAuthorize("hasAuthority('SCOPE_User') or hasAuthority('SCOPE_Admin')")
    public Map<String, String> getUserData(@AuthenticationPrincipal Jwt jwt) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User data accessed successfully");
        response.put("user", jwt.getClaim("cognito:username"));
        return response;
    }

    @GetMapping("/admin/data")
    @PreAuthorize("hasAuthority('SCOPE_Admin')")
    public Map<String, String> getAdminData(@AuthenticationPrincipal Jwt jwt) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Admin data accessed successfully");
        response.put("admin", jwt.getClaim("cognito:username"));
        response.put("sensitiveData", "Top secret information");
        return response;
    }

    @PostMapping("/data")
    public Map<String, Object> createData(@AuthenticationPrincipal Jwt jwt, @RequestBody Map<String, Object> data) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Data created successfully");
        response.put("createdBy", jwt.getClaim("cognito:username"));
        response.put("data", data);
        return response;
    }
}
