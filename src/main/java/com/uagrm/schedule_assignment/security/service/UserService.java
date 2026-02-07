package com.uagrm.schedule_assignment.security.service;

import com.uagrm.schedule_assignment.security.dto.UserCreateDto;
import com.uagrm.schedule_assignment.security.dto.UserResponseDto;
import com.uagrm.schedule_assignment.security.dto.UserUpdateDto;
import com.uagrm.schedule_assignment.security.mapper.UserMapper;
import com.uagrm.schedule_assignment.security.entity.Role;
import com.uagrm.schedule_assignment.security.entity.User;
import com.uagrm.schedule_assignment.security.repository.RoleRepository;
import com.uagrm.schedule_assignment.security.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDto userCreate(UserCreateDto userCreate) {
        if (userRepository.findByEmail(userCreate.email()).isPresent())
            throw new RuntimeException("Error: User with email " + userCreate.email() + " already exists.");

        Integer code = generateCode();
        User user = User
                .builder()
                .code(code)
                .password(passwordEncoder.encode(userCreate.password()))
                .name(userCreate.name())
                .lastName(userCreate.lastName())
                .email(userCreate.email())
                .isActive(true)
                .build();

        if (userCreate.rolesIds() != null && !userCreate.rolesIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userCreate.rolesIds()));
            user.setRoles(roles);
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public UserResponseDto getUserById(Long id) {
        return userMapper.toDto(
                userRepository
                        .findById(id)
                        .orElseThrow( ()->new RuntimeException("User no found"))
        );
    }

    @Transactional(readOnly = true)
    public Iterable<UserResponseDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDto updateUser(Long id, @NonNull UserUpdateDto userUpdate) {
        User user = userRepository
                .findById(id)
                .orElseThrow( ()->new RuntimeException("User no found"));

        user.setName(userUpdate.name());
        user.setLastName(userUpdate.lastName());
        user.setIsActive(userUpdate.isActive());

        if (userUpdate.password() != null)
            user.setPassword(passwordEncoder.encode(userUpdate.password()));

        if (userUpdate.rolesIds() != null && !userUpdate.rolesIds().isEmpty()) {
            Set<Role> roles = new HashSet<>(roleRepository.findAllById(userUpdate.rolesIds()));
            user.setRoles(roles);
        }

        return userMapper.toDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow( ()->new RuntimeException("User no found"));

        user.setIsActive(false);

        userRepository.save(user);
    }


    private Integer generateCode() {
        int code;
        do {
            code = (int) (Math.random() * 9000) + 1000 ;
        } while (userRepository.findByCode(code).isPresent());
        return code;
    }

    public User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated())
            throw new RuntimeException("User not authenticated");

        String userCode = authentication.getName();

        return userRepository
                .findByCode(Integer.parseInt(userCode))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
