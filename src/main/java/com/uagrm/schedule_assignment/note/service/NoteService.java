package com.uagrm.schedule_assignment.note.service;


import com.uagrm.schedule_assignment.note.dto.NoteRequestDto;
import com.uagrm.schedule_assignment.note.dto.NoteResponseDto;
import com.uagrm.schedule_assignment.note.entity.Note;
import com.uagrm.schedule_assignment.note.mapper.NoteMapper;
import com.uagrm.schedule_assignment.note.repository.NoteRepository;
import com.uagrm.schedule_assignment.security.entity.User;
import com.uagrm.schedule_assignment.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoteService {
    private final NoteRepository noteRepository;
    private final UserService userService;
    private final NoteMapper noteMapper;

    @Transactional
    public NoteResponseDto createNote(NoteRequestDto requestDto) {
        User userCurrent = userService.getCurrentUser();

        Note note = noteMapper.toEntity(requestDto);
        note.setUser(userCurrent);

        return noteMapper.toDto(noteRepository.save(note));
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
        noteRepository.deleteById(id);
    }
}
