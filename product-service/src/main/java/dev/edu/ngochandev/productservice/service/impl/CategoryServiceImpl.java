package dev.edu.ngochandev.productservice.service.impl;

import dev.edu.ngochandev.productservice.dto.req.CreateCategoryRequestDto;
import dev.edu.ngochandev.productservice.dto.res.CategoryResponseDto;
import dev.edu.ngochandev.productservice.entity.CategoryEntity;
import dev.edu.ngochandev.productservice.mapper.CategoryMapper;
import dev.edu.ngochandev.productservice.repository.CategoryRepository;
import dev.edu.ngochandev.productservice.service.CategoryService;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
import dev.edu.ngochandev.sharedkernel.exception.DuplicateResourceException;
import dev.edu.ngochandev.sharedkernel.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryResponseDto createCategory(CreateCategoryRequestDto req) {
        //check name and slug exist
        if(categoryRepository.existsByName((req.getName()))) throw new DuplicateResourceException("error.category.name.exists");
        if(categoryRepository.existsBySlug(req.getSlug())) throw new DuplicateResourceException("error.category.slug.exists");

        //save to db
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(req.getName());
        categoryEntity.setDescription(req.getDescription());
        categoryEntity.setSlug(req.getSlug());
        categoryEntity.setThumbnail(req.getThumbnail());
        //check parentId exist
        if(req.getParentId() != null && !req.getParentId().isEmpty()){
            CategoryEntity parent = categoryRepository.findById(req.getParentId()).orElse(null);
            if(parent == null) throw new ResourceNotFoundException("parent category not found");
            categoryEntity.setParent(parent);
        }
        categoryEntity = categoryRepository.save(categoryEntity);

        return categoryMapper.toCategoryResponseDto(categoryEntity);
    }

    @Override
    public PageResponseDto<CategoryResponseDto> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAllByLevel(1);
        if(categories.isEmpty()) return PageResponseDto.<CategoryResponseDto>builder().build();
        List<CategoryResponseDto> categoryResponseDtos = categories.stream().map(categoryMapper::toCategoryResponseDto).toList();
        return PageResponseDto.<CategoryResponseDto>builder()
                .totalItems(categories.size())
                .totalPages(1)
                .items(categoryResponseDtos)
                .build();
    }

    @Override
    public void updateCategoryStatus(String id, boolean active) {
        CategoryEntity cate = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.category.not-found"));
        updateStatusRecursively(cate, active);
    }

    private void updateStatusRecursively(CategoryEntity category, boolean active) {
        if (Objects.equals(category.getIsActive(), active)) {
            return;
        }

        category.setIsActive(active);
        categoryRepository.save(category);

        if (category.getChildren() != null && !category.getChildren().isEmpty()) {
            for (CategoryEntity child : category.getChildren()) {
                updateStatusRecursively(child, active);
            }
        }
    }
}
