package com.uagrm.personal.workshop.repository;

import com.uagrm.personal.workshop.entity.RecipeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeDetailRepository extends JpaRepository<RecipeDetail, Long> {
    List<RecipeDetail> findAllByRecipeId(Long recipeId);
}
