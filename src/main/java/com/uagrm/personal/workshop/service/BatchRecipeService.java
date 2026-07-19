package com.uagrm.personal.workshop.service;

import com.uagrm.personal.workshop.dto.BatchRecipeRequestDto;
import com.uagrm.personal.workshop.dto.BatchRecipeResponseDto;
import com.uagrm.personal.workshop.dto.RecipeDetailRequestDto;
import com.uagrm.personal.workshop.dto.RecipeDetailResponseDto;
import com.uagrm.personal.workshop.entity.BatchRecipe;
import com.uagrm.personal.workshop.entity.Material;
import com.uagrm.personal.workshop.entity.PictureFrame;
import com.uagrm.personal.workshop.entity.RecipeDetail;
import com.uagrm.personal.workshop.mapper.BatchRecipeMapper;
import com.uagrm.personal.workshop.mapper.RecipeDetailMapper;
import com.uagrm.personal.workshop.repository.BatchRecipeRepository;
import com.uagrm.personal.workshop.repository.RecipeDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BatchRecipeService {
    private final BatchRecipeRepository batchRecipeRepository;
    private final RecipeDetailRepository recipeDetailRepository;
    private final BatchRecipeMapper batchRecipeMapper;
    private final RecipeDetailMapper recipeDetailMapper;
    private final PictureFrameService pictureFrameService;
    private final MaterialService materialService;

    @Transactional
    public BatchRecipeResponseDto createRecipe(BatchRecipeRequestDto requestDto) {
        PictureFrame frame = pictureFrameService.findFrame(requestDto.frameId());

        BatchRecipe recipe = BatchRecipe.builder()
                .frame(frame)
                .batchQuantity(requestDto.batchQuantity())
                .description(requestDto.description())
                .build();

        return batchRecipeMapper.toDto(batchRecipeRepository.save(recipe));
    }

    @Transactional(readOnly = true)
    public Iterable<BatchRecipeResponseDto> getAllRecipes() {
        return batchRecipeRepository.findAll().stream().map(batchRecipeMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BatchRecipeResponseDto getRecipeById(Long id) {
        return batchRecipeMapper.toDto(findRecipe(id));
    }

    @Transactional
    public BatchRecipeResponseDto updateRecipe(Long id, BatchRecipeRequestDto requestDto) {
        BatchRecipe recipe = findRecipe(id);
        recipe.setFrame(pictureFrameService.findFrame(requestDto.frameId()));
        recipe.setBatchQuantity(requestDto.batchQuantity());
        recipe.setDescription(requestDto.description());
        return batchRecipeMapper.toDto(batchRecipeRepository.save(recipe));
    }

    @Transactional
    public void deleteRecipe(Long id) {
        batchRecipeRepository.deleteById(id);
    }

    @Transactional
    public RecipeDetailResponseDto addRecipeDetail(Long recipeId, RecipeDetailRequestDto requestDto) {
        BatchRecipe recipe = findRecipe(recipeId);
        Material material = materialService.findMaterial(requestDto.materialId());

        RecipeDetail detail = RecipeDetail.builder()
                .recipe(recipe)
                .material(material)
                .requiredQuantity(requestDto.requiredQuantity())
                .build();

        return recipeDetailMapper.toDto(recipeDetailRepository.save(detail));
    }

    @Transactional(readOnly = true)
    public Iterable<RecipeDetailResponseDto> getRecipeDetails(Long recipeId) {
        return recipeDetailRepository.findAllByRecipeId(recipeId).stream().map(recipeDetailMapper::toDto).toList();
    }

    @Transactional
    public RecipeDetailResponseDto updateRecipeDetail(Long recipeId, Long detailId, RecipeDetailRequestDto requestDto) {
        RecipeDetail detail = findRecipeDetail(recipeId, detailId);
        detail.setMaterial(materialService.findMaterial(requestDto.materialId()));
        detail.setRequiredQuantity(requestDto.requiredQuantity());
        return recipeDetailMapper.toDto(recipeDetailRepository.save(detail));
    }

    @Transactional
    public void deleteRecipeDetail(Long recipeId, Long detailId) {
        recipeDetailRepository.delete(findRecipeDetail(recipeId, detailId));
    }

    BatchRecipe findRecipe(Long id) {
        return batchRecipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch recipe not found"));
    }

    private RecipeDetail findRecipeDetail(Long recipeId, Long detailId) {
        RecipeDetail detail = recipeDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Recipe detail not found"));
        if (!detail.getRecipe().getId().equals(recipeId)) {
            throw new RuntimeException("Recipe detail does not belong to recipe " + recipeId);
        }
        return detail;
    }
}
