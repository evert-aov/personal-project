package com.uagrm.schedule_assignment.note.service;

import com.uagrm.schedule_assignment.note.dto.WhiteboardRequestDto;
import com.uagrm.schedule_assignment.note.dto.WhiteboardResponseDto;
import com.uagrm.schedule_assignment.note.entity.Whiteboard;
import com.uagrm.schedule_assignment.note.mapper.WhiteboardMapper;
import com.uagrm.schedule_assignment.note.repository.WhiteboardRepository;
import com.uagrm.schedule_assignment.security.entity.User;
import com.uagrm.schedule_assignment.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WhiteboardService {
    private final WhiteboardRepository whiteboardRepository;
    private final UserService userService;
    private final WhiteboardMapper whiteboardMapper;

    @Transactional
    public WhiteboardResponseDto createWhiteboard(WhiteboardRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();

        Whiteboard whiteboard = whiteboardMapper.toEntity(requestDto);
        whiteboard.setUser(userCurrent);

        return whiteboardMapper.toDto(whiteboardRepository.save(whiteboard));
    }

    @Transactional(readOnly = true)
    public WhiteboardResponseDto getWhiteboardById(Long id) {

        User userCurrent = userService.getCurrentUser();
        Whiteboard whiteboard = whiteboardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Whiteboard not found"));

        if (!userCurrent.isAdmin() && !whiteboard.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to view this record.");

        return whiteboardMapper.toDto(whiteboard);
    }

    @Transactional(readOnly = true)
    public Iterable<WhiteboardResponseDto> getAllWhiteboards() {
        User userCurrent = userService.getCurrentUser();

        return whiteboardRepository.findAllByUserId(userCurrent.getId())
                .stream()
                .map(whiteboardMapper::toDto)
                .toList();
    }

    @Transactional
    public WhiteboardResponseDto updateWhiteboard(Long id, WhiteboardRequestDto requestDto) {
        Whiteboard whiteboard = whiteboardRepository.findById(id).orElseThrow(() -> new RuntimeException("Whiteboard not found"));

        User userCurrent = userService.getCurrentUser();
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !whiteboard.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to update this record.");


        whiteboard.setTitle(requestDto.title());
        whiteboard.setPreview_image_url(requestDto.preview_image_url());
        whiteboard.setContent(requestDto.content());

        return whiteboardMapper.toDto(whiteboardRepository.save(whiteboard));
    }

    @Transactional
    public void deleteWhiteboard(Long id) {
        Whiteboard whiteboard = whiteboardRepository.findById(id).orElseThrow(() -> new RuntimeException("Whiteboard not found"));

        User userCurrent = userService.getCurrentUser();
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !whiteboard.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to delete this record.");

        whiteboardRepository.deleteById(id);
    }
}
