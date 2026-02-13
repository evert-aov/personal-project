package com.uagrm.schedule_assignment.security.service;

import com.uagrm.schedule_assignment.security.dto.ProfileUpdateDto;
import com.uagrm.schedule_assignment.security.dto.UserResponseDto;
import com.uagrm.schedule_assignment.security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto updateProfile(ProfileUpdateDto profileUpdateDto) {
        User userCurrent = userService.getCurrentUser();

        userCurrent.setName(profileUpdateDto.name());
        userCurrent.setLastName(profileUpdateDto.lastName());

        if (profileUpdateDto.password() != null)
            userCurrent.setPassword(passwordEncoder.encode(profileUpdateDto.password()));

        return userService.getUserById(userCurrent.getId());
    }

    @Transactional
    public UserResponseDto getUserProfile() {
        return userService.getUserById(userService.getCurrentUser().getId());
    }
}
