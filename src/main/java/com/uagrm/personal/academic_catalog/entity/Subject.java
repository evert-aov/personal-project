package com.uagrm.personal.academic_catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "subjects")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    private String code;

    @Column(nullable = false, length = 80)
    private String name;

    @PrePersist
    @PreUpdate
    private void onSaveOrUpdate() {
        if (this.code != null)
            this.code = this.code.toUpperCase().trim();

        if (this.name != null)
            this.name = this.name.toUpperCase().trim();
    }
}
