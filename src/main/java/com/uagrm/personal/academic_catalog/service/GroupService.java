package com.uagrm.personal.academic_catalog.service;

import com.uagrm.personal.academic_catalog.dto.GroupRequestDto;
import com.uagrm.personal.academic_catalog.dto.GroupResponseDto;
import com.uagrm.personal.academic_catalog.entity.Group;
import com.uagrm.personal.academic_catalog.mapper.GroupMapper;
import com.uagrm.personal.academic_catalog.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public GroupResponseDto createGroup(GroupRequestDto requestDto) {
        return groupMapper.toDto(groupRepository.save(groupMapper.toEntity(requestDto)));
    }

    @Transactional(readOnly = true)
    public Iterable<GroupResponseDto> getAllGroups() {
        return groupRepository.findAll()
                .stream()
                .map(groupMapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public GroupResponseDto getGroupById(Long id) {
        return groupMapper.toDto(groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found")));
    }

    @Transactional
    public GroupResponseDto updateGroup(Long id, GroupRequestDto requestDto) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new RuntimeException("Group not found"));
        group.setName(requestDto.name());
        return groupMapper.toDto(groupRepository.save(group));
    }

    @Transactional
    public void deleteGroup(Long id) {
        groupRepository.deleteById(id);
    }
}
