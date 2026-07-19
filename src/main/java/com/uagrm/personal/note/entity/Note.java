package com.uagrm.personal.note.entity;

import com.uagrm.personal.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "notes")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    // Rich-text content edited from the mobile (Quill) note editor.
    @Column(columnDefinition = "TEXT")
    private String content;

    // Version of the file backing the web (OnlyOffice) editor for this note.
    @Column(name = "document_key", nullable = false, length = 100)
    private String documentKey;

    // docx, odt, xlsx, ods or pdf -- drives which OnlyOffice editor opens and the file's format.
    @Column(name = "file_type", nullable = false, length = 10)
    private String fileType;
}
