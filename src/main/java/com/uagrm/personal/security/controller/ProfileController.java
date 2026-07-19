package com.uagrm.personal.security.controller;

import com.uagrm.personal.security.dto.ProfileUpdateDto;
import com.uagrm.personal.security.dto.UserResponseDto;
import com.uagrm.personal.security.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    ResponseEntity<UserResponseDto> getProfile() {
        return ResponseEntity.ok(profileService.getUserProfile());
    }

    @PostMapping
    ResponseEntity<UserResponseDto> updateProfile(@RequestBody ProfileUpdateDto profileUpdateDto) {
        return ResponseEntity.ok(profileService.updateProfile(profileUpdateDto));
    }
}
