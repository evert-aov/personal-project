package com.uagrm.schedule_assignment.note.mapper;

import com.uagrm.schedule_assignment.note.dto.NoteRequestDto;
import com.uagrm.schedule_assignment.note.dto.NoteResponseDto;
import com.uagrm.schedule_assignment.note.entity.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoteMapper {
    public Note toEntity(NoteRequestDto dto) {
        if (dto == null) return null;

        return Note.builder()
                .title(dto.title())
                .content(dto.content())
                .build();
    }

    public NoteResponseDto toDto(Note entity) {
        return new NoteResponseDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getTitle(),
                entity.getContent()
        );
    }
}
