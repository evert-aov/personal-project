package com.uagrm.personal.academic_catalog.controller;

import com.uagrm.personal.academic_catalog.dto.GroupRequestDto;
import com.uagrm.personal.academic_catalog.dto.GroupResponseDto;
import com.uagrm.personal.academic_catalog.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@RequestBody GroupRequestDto requestDto) {
        return new ResponseEntity<>(groupService.createGroup(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<GroupResponseDto>> getAllGroups() {
        return ResponseEntity.ok(groupService.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDto> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GroupResponseDto> updateGroup(@PathVariable Long id, @RequestBody GroupRequestDto requestDto) {
        return ResponseEntity.ok(groupService.updateGroup(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable Long id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}
