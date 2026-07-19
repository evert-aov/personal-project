package com.uagrm.personal.workshop.controller;

import com.uagrm.personal.workshop.dto.PictureFrameRequestDto;
import com.uagrm.personal.workshop.dto.PictureFrameResponseDto;
import com.uagrm.personal.workshop.service.PictureFrameService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workshop/frames")
@RequiredArgsConstructor
@Tag(name = "Workshop - Picture Frames", description = "Catálogo de marcos (marcos)")
public class PictureFrameController {
    private final PictureFrameService pictureFrameService;

    @PostMapping
    public ResponseEntity<PictureFrameResponseDto> createFrame(@RequestBody PictureFrameRequestDto requestDto) {
        return new ResponseEntity<>(pictureFrameService.createFrame(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<PictureFrameResponseDto>> getAllFrames() {
        return ResponseEntity.ok(pictureFrameService.getAllFrames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PictureFrameResponseDto> getFrameById(@PathVariable Long id) {
        return ResponseEntity.ok(pictureFrameService.getFrameById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PictureFrameResponseDto> updateFrame(@PathVariable Long id, @RequestBody PictureFrameRequestDto requestDto) {
        return ResponseEntity.ok(pictureFrameService.updateFrame(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFrame(@PathVariable Long id) {
        pictureFrameService.deleteFrame(id);
        return ResponseEntity.noContent().build();
    }
}
