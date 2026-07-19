package com.uagrm.personal.note.mapper;

import com.uagrm.personal.note.dto.WhiteboardRequestDto;
import com.uagrm.personal.note.dto.WhiteboardResponseDto;
import com.uagrm.personal.note.entity.Whiteboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WhiteboardMapper {

    public Whiteboard toEntity(WhiteboardRequestDto dto) {
        if (dto == null) return null;

        return Whiteboard.builder()
                .title(dto.title())
                .content(dto.content())
                .preview_image_url(dto.preview_image_url())
                .build();
    }

    public WhiteboardResponseDto toDto(Whiteboard whiteboard) {
        return new WhiteboardResponseDto(
                whiteboard.getId(),
                whiteboard.getUser().getId(),
                whiteboard.getTitle(),
                whiteboard.getContent(),
                whiteboard.getPreview_image_url()
        );
    }
}
