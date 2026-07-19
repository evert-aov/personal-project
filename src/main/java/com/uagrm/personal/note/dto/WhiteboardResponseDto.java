package com.uagrm.personal.note.dto;

public record WhiteboardResponseDto(
        Long id,
        Long userId,
        String title,
        String content,
        String preview_image_url
) {
}
