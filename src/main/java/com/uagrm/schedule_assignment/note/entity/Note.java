package com.uagrm.schedule_assignment.note.entity;

import com.uagrm.schedule_assignment.security.entity.User;
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

    @Column(columnDefinition = "TEXT")
    private String content;
}
