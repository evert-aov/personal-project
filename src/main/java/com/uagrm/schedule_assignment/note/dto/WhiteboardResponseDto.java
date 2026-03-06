package com.uagrm.schedule_assignment.note.dto;

public record WhiteboardResponseDto(
        Long id,
        Long userId,
        String title,
        String content,
        String preview_image_url
) {
}
