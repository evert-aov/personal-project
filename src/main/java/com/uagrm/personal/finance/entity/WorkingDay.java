package com.uagrm.personal.finance.entity;

import com.uagrm.personal.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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


    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 9)
    private  DayStatus status;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal amountWon = BigDecimal.ZERO;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    public boolean isFullyPaid() {
        return paidAmount.compareTo(amountWon) >= 0;
    }

    public BigDecimal getRemainingAmount() {
        return amountWon.subtract(paidAmount);
    }
}
