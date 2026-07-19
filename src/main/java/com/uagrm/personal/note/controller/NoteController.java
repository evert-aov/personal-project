package com.uagrm.personal.note.controller;

import com.uagrm.personal.note.dto.NoteRequestDto;
import com.uagrm.personal.note.dto.NoteResponseDto;
import com.uagrm.personal.note.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @GetMapping("/{id}/editor-config")
    @Operation(summary = "Obtener la config firmada para abrir la nota en OnlyOffice")
    public ResponseEntity<Map<String, Object>> getEditorConfig(@PathVariable Long id) {
        return ResponseEntity.ok(noteService.getEditorConfig(id));
    }

    /**
     * Fetched by OnlyOffice Document Server itself -- publicly reachable, see SecurityConfig.
     */
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        NoteService.NoteFile file = noteService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.mimeType()))
                .body(file.content());
    }

    /**
     * Save callback invoked by OnlyOffice Document Server -- publicly reachable,
     * but the JWT it attaches is verified inside the service before anything is trusted.
     */
    @PostMapping("/{id}/callback")
    public ResponseEntity<Map<String, Object>> handleCallback(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        String token = (authHeader != null && authHeader.startsWith("Bearer "))
                ? authHeader.substring(7)
                : String.valueOf(body.get("token"));

        noteService.handleCallback(id, body, token);
        return ResponseEntity.ok(Map.of("error", 0));
    }
}
