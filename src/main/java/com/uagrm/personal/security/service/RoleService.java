package com.uagrm.personal.security.service;

import com.uagrm.personal.security.dto.RoleRequestDto;
import com.uagrm.personal.security.dto.RoleResponseDto;
import com.uagrm.personal.security.entity.Role;
import com.uagrm.personal.security.mapper.RoleMapper;
import com.uagrm.personal.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponseDto createRole(RoleRequestDto requestDto) {
        if (requestDto.name() == null || requestDto.name().isBlank()) {
            throw new RuntimeException("Role name is required");
        }
        if (roleRepository.findByName(requestDto.name()).isPresent()) {
            throw new RuntimeException("Role with name '" + requestDto.name() + "' already exists");
        }

        Role role = roleMapper.toEntity(requestDto);
        Role savedRole = roleRepository.save(role);
        return roleMapper.toDto(savedRole);
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoleResponseDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return roleMapper.toDto(role);
    }

    @Transactional
    public RoleResponseDto updateRole(Long id, RoleRequestDto requestDto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        if (requestDto.name() != null && !requestDto.name().isBlank()) {
            role.setName(requestDto.name());
        }
        if (requestDto.description() != null) {
            role.setDescription(requestDto.description());
        }
        if (requestDto.level() != null) {
            role.setLevel(requestDto.level());
        }
        if (requestDto.isActive() != null) {
            role.setIsActive(requestDto.isActive());
        }

        Role updatedRole = roleRepository.save(role);
        return roleMapper.toDto(updatedRole);
    }

    @Transactional
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        role.setIsActive(false);
        roleRepository.save(role);
    }
}
