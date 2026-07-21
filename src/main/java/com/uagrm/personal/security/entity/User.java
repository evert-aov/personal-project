package com.uagrm.personal.security.entity;

import com.uagrm.personal.finance.entity.WorkingDay;
import jakarta.persistence.*;
import lombok.*; // Importa todo lombok
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private String lastName;

    @Column(nullable = false)
    private String email;

    private Boolean isActive;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<WorkingDay> workingDays = new HashSet<>();

    // Saldo a favor del usuario (anticipos pagados que aún no se aplicaron a ninguna deuda).
    @Column(name = "advance_balance", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal advanceBalance = BigDecimal.ZERO;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null) return Set.of();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isEnabled() {
        return Boolean.TRUE.equals(isActive);
    }

    public boolean hasRoles(String roleName) {
        if (roles == null) return false;
        return roles.stream().anyMatch(role -> role.getName() != null && role.getName().equalsIgnoreCase(roleName));
    }

    public boolean isSuperuser() {
        return hasRoles("SUPERUSER");
    }

    public boolean isAdmin() {
        return hasRoles("ADMIN") || isSuperuser();
    }
}