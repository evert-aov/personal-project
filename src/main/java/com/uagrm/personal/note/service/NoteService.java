package com.uagrm.personal.note.service;


import com.uagrm.personal.note.dto.NoteRequestDto;
import com.uagrm.personal.note.dto.NoteResponseDto;
import com.uagrm.personal.note.entity.Note;
import com.uagrm.personal.note.mapper.NoteMapper;
import com.uagrm.personal.note.repository.NoteRepository;
import com.uagrm.personal.security.entity.User;
import com.uagrm.personal.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoteService {
    private static final String RESOURCE_PATH = "notes";

    private final NoteRepository noteRepository;
    private final UserService userService;
    private final NoteMapper noteMapper;
    private final NoteFileStorage fileStorage;
    private final OnlyOfficeService onlyOfficeService;
    private final RestClient restClient = RestClient.create();

    @Transactional
    public NoteResponseDto createNote(NoteRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();

        Note note = noteMapper.toEntity(requestDto);
        note.setUser(userCurrent);
        note.setDocumentKey(UUID.randomUUID().toString());

        note = noteRepository.save(note);
        fileStorage.createBlankDocument(note.getId(), note.getFileType());

        return noteMapper.toDto(note);
    }

    @Transactional(readOnly = true)
    public NoteResponseDto getNoteById(Long id) {
        User userCurrent = userService.getCurrentUser();
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!userCurrent.isAdmin() && !note.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to view this record.");

        return noteMapper.toDto(note);
    }

    @Transactional(readOnly = true)
    public Iterable<NoteResponseDto> getAllNotes() {
        User userCurrent = userService.getCurrentUser();

        return noteRepository.findAllByUserId(userCurrent.getId())
                .stream()
                .map(noteMapper::toDto).toList();
    }

    @Transactional
    public NoteResponseDto updateNote(Long id, NoteRequestDto requestDto) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));

        User userCurrent = userService.getCurrentUser();
        boolean isAdmin = userCurrent.isAdmin();

        if (!isAdmin && !note.getUser().getId().equals(userCurrent.getId())) {
            throw new RuntimeException("You do not have permission to update this record.");
        }

        note.setTitle(requestDto.title());
        note.setContent(requestDto.content());

        return noteMapper.toDto(noteRepository.save(note));
    }

    @Transactional
    public void deleteNote(Long id) {
        User userCurrent = userService.getCurrentUser();
        Note note = noteRepository.findById(id).orElseThrow(() -> new RuntimeException("Note not found"));

        if (!userCurrent.isAdmin() && !note.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to delete this record.");

        noteRepository.deleteById(id);
        fileStorage.delete(id, note.getFileType());
    }

    /**
     * Builds the JWT-signed config the web client hands straight to OnlyOffice's DocsAPI.DocEditor.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getEditorConfig(Long id) {
        User userCurrent = userService.getCurrentUser();
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!userCurrent.isAdmin() && !note.getUser().getId().equals(userCurrent.getId()))
            throw new RuntimeException("You do not have permission to view this record.");

        return onlyOfficeService.buildEditorConfig(RESOURCE_PATH, note.getId(), note.getTitle(), note.getDocumentKey(), note.getFileType(), userCurrent);
    }

    /**
     * Serves the raw file bytes plus their MIME type. Called by the Document Server
     * itself (no end-user auth context), so this must stay a public endpoint -- see SecurityConfig.
     */
    @Transactional(readOnly = true)
    public NoteFile getFile(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        return new NoteFile(fileStorage.read(id, note.getFileType()), onlyOfficeService.mimeTypeFor(note.getFileType()));
    }

    public record NoteFile(byte[] content, String mimeType) {
    }

    /**
     * Handles OnlyOffice's save callback. Status 2 (MustSave) and 6 (MustForceSave)
     * mean a new version is ready at `url`; we pull it down, persist it, and bump
     * the document key so the document server treats it as a new version.
     */
    @Transactional
    public void handleCallback(Long id, Map<String, Object> body, String token) {
        if (!onlyOfficeService.isValidToken(token))
            throw new RuntimeException("Invalid or missing OnlyOffice callback token");

        if (!noteRepository.existsById(id))
            throw new RuntimeException("Note not found");

        int status = body.get("status") instanceof Number n ? n.intValue() : -1;

        if (status == 2 || status == 6) {
            String url = (String) body.get("url");
            if (url == null || url.isBlank()) return;

            byte[] updatedDocument = restClient.get().uri(url).retrieve().body(byte[].class);
            if (updatedDocument == null) return;

            Note note = noteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Note not found"));

            fileStorage.write(id, note.getFileType(), updatedDocument);
            note.setDocumentKey(UUID.randomUUID().toString());
            noteRepository.save(note);
        }
    }
}
