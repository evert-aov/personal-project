package com.uagrm.personal.note.dto;

public record NoteResponseDto(
        Long id,
        Long userId,
        String title,
        String content,
        String documentKey,
        String fileType
) {
}
