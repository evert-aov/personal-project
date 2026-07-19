package com.uagrm.personal.note.dto;

public record WhiteboardRequestDto(
        String title,
        String content,
        String preview_image_url
) {
}
