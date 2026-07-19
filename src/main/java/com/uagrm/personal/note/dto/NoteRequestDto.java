package com.uagrm.personal.note.dto;

public record NoteRequestDto(
        String title,
        String content,
        String fileType
) {
}
