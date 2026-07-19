package com.uagrm.personal.workshop.controller;

import com.uagrm.personal.workshop.dto.BatchRecipeRequestDto;
import com.uagrm.personal.workshop.dto.BatchRecipeResponseDto;
import com.uagrm.personal.workshop.dto.RecipeDetailRequestDto;
import com.uagrm.personal.workshop.dto.RecipeDetailResponseDto;
import com.uagrm.personal.workshop.service.BatchRecipeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workshop/recipes")
@RequiredArgsConstructor
@Tag(name = "Workshop - Batch Recipes", description = "Recetas de lote y su detalle (recetas_lote, detalle_receta)")
public class BatchRecipeController {
    private final BatchRecipeService batchRecipeService;

    @PostMapping
    public ResponseEntity<BatchRecipeResponseDto> createRecipe(@RequestBody BatchRecipeRequestDto requestDto) {
        return new ResponseEntity<>(batchRecipeService.createRecipe(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<BatchRecipeResponseDto>> getAllRecipes() {
        return ResponseEntity.ok(batchRecipeService.getAllRecipes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchRecipeResponseDto> getRecipeById(@PathVariable Long id) {
        return ResponseEntity.ok(batchRecipeService.getRecipeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchRecipeResponseDto> updateRecipe(@PathVariable Long id, @RequestBody BatchRecipeRequestDto requestDto) {
        return ResponseEntity.ok(batchRecipeService.updateRecipe(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        batchRecipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{recipeId}/details")
    public ResponseEntity<RecipeDetailResponseDto> addRecipeDetail(@PathVariable Long recipeId,
                                                                     @RequestBody RecipeDetailRequestDto requestDto) {
        return new ResponseEntity<>(batchRecipeService.addRecipeDetail(recipeId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{recipeId}/details")
    public ResponseEntity<Iterable<RecipeDetailResponseDto>> getRecipeDetails(@PathVariable Long recipeId) {
        return ResponseEntity.ok(batchRecipeService.getRecipeDetails(recipeId));
    }

    @PutMapping("/{recipeId}/details/{detailId}")
    public ResponseEntity<RecipeDetailResponseDto> updateRecipeDetail(@PathVariable Long recipeId,
                                                                        @PathVariable Long detailId,
                                                                        @RequestBody RecipeDetailRequestDto requestDto) {
        return ResponseEntity.ok(batchRecipeService.updateRecipeDetail(recipeId, detailId, requestDto));
    }

    @DeleteMapping("/{recipeId}/details/{detailId}")
    public ResponseEntity<Void> deleteRecipeDetail(@PathVariable Long recipeId, @PathVariable Long detailId) {
        batchRecipeService.deleteRecipeDetail(recipeId, detailId);
        return ResponseEntity.noContent().build();
    }
}
