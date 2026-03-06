package com.uagrm.schedule_assignment.note.controller;

import com.uagrm.schedule_assignment.note.dto.WhiteboardRequestDto;
import com.uagrm.schedule_assignment.note.dto.WhiteboardResponseDto;
import com.uagrm.schedule_assignment.note.service.WhiteboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whiteboards")
@RequiredArgsConstructor
public class WhiteboardController {
    private final WhiteboardService whiteboardService;

    @PostMapping
    public ResponseEntity<WhiteboardResponseDto> createWhiteboard(@RequestBody WhiteboardRequestDto requestDto){
        return new ResponseEntity<>(whiteboardService.createWhiteboard(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WhiteboardResponseDto> getWhiteboardById(@PathVariable Long id){
        return ResponseEntity.ok(whiteboardService.getWhiteboardById(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<WhiteboardResponseDto>> getAllWhiteboards(){
        return ResponseEntity.ok(whiteboardService.getAllWhiteboards());
    }

    @PutMapping("/{id}")
    public ResponseEntity<WhiteboardResponseDto> updateWhiteboard(@PathVariable Long id, @RequestBody WhiteboardRequestDto requestDto){
        return ResponseEntity.ok(whiteboardService.updateWhiteboard(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWhiteboard(@PathVariable Long id){
        whiteboardService.deleteWhiteboard(id);
        return ResponseEntity.noContent().build();
    }
}
