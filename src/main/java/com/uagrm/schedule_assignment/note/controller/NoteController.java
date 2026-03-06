package com.uagrm.schedule_assignment.note.controller;

import com.uagrm.schedule_assignment.note.dto.NoteRequestDto;
import com.uagrm.schedule_assignment.note.dto.NoteResponseDto;
import com.uagrm.schedule_assignment.note.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
@Tag(name = "Notes", description = "Endpoints para la gestión de notas personales")
public class NoteController {
    private final NoteService noteService;

    @PostMapping
    @Operation(summary = "Crear una nueva nota")
    public ResponseEntity<NoteResponseDto> createNote(@RequestBody NoteRequestDto requestDto){
        return new ResponseEntity<>(noteService.createNote(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una nota por ID")
    public ResponseEntity<NoteResponseDto> getNoteById(@PathVariable Long id){
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    @GetMapping
    public ResponseEntity<Iterable<NoteResponseDto>> getAllNotes(){
        return ResponseEntity.ok(noteService.getAllNotes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDto> updateNote(@PathVariable Long id, @RequestBody NoteRequestDto requestDto){
        return ResponseEntity.ok(noteService.updateNote(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable Long id){
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
