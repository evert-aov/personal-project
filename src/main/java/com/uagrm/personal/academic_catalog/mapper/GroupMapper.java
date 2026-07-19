package com.uagrm.personal.academic_catalog.mapper;

import com.uagrm.personal.academic_catalog.dto.GroupRequestDto;
import com.uagrm.personal.academic_catalog.dto.GroupResponseDto;
import com.uagrm.personal.academic_catalog.entity.Group;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupMapper {
    public Group toEntity(GroupRequestDto dto) {
        if (dto == null) return null;

        return Group.builder()
                .name(dto.name())
                .build();
    }

    public GroupResponseDto toDto(@NonNull Group group) {
        return new GroupResponseDto(
                group.getId(),
                group.getName()
        );
    }
}
