package com.uagrm.schedule_assignment.academic_catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "\"groups\"")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 3)
    private String name;

    @PrePersist
    @PreUpdate
    private void onSaveOrUpdate() {
        if (this.name != null) {
            this.name = this.name.toUpperCase().trim();
        }
    }
}
