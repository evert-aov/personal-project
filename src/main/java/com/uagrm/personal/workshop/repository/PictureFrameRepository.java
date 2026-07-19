package com.uagrm.personal.workshop.repository;

import com.uagrm.personal.workshop.entity.PictureFrame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureFrameRepository extends JpaRepository<PictureFrame, Long> {
}
