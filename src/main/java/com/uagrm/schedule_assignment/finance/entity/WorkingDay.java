package com.uagrm.schedule_assignment.finance.entity;

import com.uagrm.schedule_assignment.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(
        name = "working_days",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"user_id", "date"}
                )
        }
)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkingDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workingDay")
    private Set<Transaction> transaction = new HashSet<>();

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 9)
    private  DayStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal amountWon = BigDecimal.ZERO;
}
