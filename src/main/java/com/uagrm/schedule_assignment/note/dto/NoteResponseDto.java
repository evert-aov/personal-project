package com.uagrm.schedule_assignment.note.dto;

public record NoteResponseDto(
        Long id,
        Long userId,
        String title,
        String content
) {
}
