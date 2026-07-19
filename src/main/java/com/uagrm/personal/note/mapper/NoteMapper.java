package com.uagrm.personal.note.mapper;

import com.uagrm.personal.note.dto.NoteRequestDto;
import com.uagrm.personal.note.dto.NoteResponseDto;
import com.uagrm.personal.note.entity.Note;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class NoteMapper {
    private static final Set<String> SUPPORTED_FILE_TYPES = Set.of("docx", "odt", "xlsx", "ods", "pdf");
    private static final String DEFAULT_FILE_TYPE = "docx";

    public Note toEntity(NoteRequestDto dto) {
        if (dto == null) return null;

        return Note.builder()
                .title(dto.title())
                .content(dto.content())
                .fileType(resolveFileType(dto.fileType()))
                .build();
    }

    public NoteResponseDto toDto(Note entity) {
        return new NoteResponseDto(
                entity.getId(),
                entity.getUser().getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getDocumentKey(),
                entity.getFileType()
        );
    }

    private String resolveFileType(String requested) {
        if (requested == null || !SUPPORTED_FILE_TYPES.contains(requested)) {
            return DEFAULT_FILE_TYPE;
        }
        return requested;
    }
}
