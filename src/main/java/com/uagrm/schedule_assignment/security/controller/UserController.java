package com.uagrm.schedule_assignment.security.controller;

import com.uagrm.schedule_assignment.security.dto.UserCreateDto;
import com.uagrm.schedule_assignment.security.dto.UserResponseDto;
import com.uagrm.schedule_assignment.security.dto.UserUpdateDto;
import com.uagrm.schedule_assignment.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/security/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.userCreate(userCreateDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdate) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
