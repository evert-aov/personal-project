package com.uagrm.personal.note.repository;

import com.uagrm.personal.note.entity.Whiteboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WhiteboardRepository extends JpaRepository<Whiteboard, Long> {
    List<Whiteboard> findAllByUserId(Long userId);
}
