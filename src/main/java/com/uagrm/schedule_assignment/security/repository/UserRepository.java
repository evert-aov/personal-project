package com.uagrm.schedule_assignment.security.repository;

import com.uagrm.schedule_assignment.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.code = ?1")
    Optional<User> findByCode(Integer code);

    boolean existsByEmail(String email);

    boolean existsByCode(Integer code);
}
