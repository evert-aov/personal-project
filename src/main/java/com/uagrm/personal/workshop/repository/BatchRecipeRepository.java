package com.uagrm.personal.workshop.repository;

import com.uagrm.personal.workshop.entity.BatchRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchRecipeRepository extends JpaRepository<BatchRecipe, Long> {
}
