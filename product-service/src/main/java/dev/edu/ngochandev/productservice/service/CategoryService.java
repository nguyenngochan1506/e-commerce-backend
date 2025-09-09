package dev.edu.ngochandev.productservice.service;

import dev.edu.ngochandev.productservice.dto.req.CreateCategoryRequestDto;
import dev.edu.ngochandev.productservice.dto.req.UpdateCategoryRequestDto;
import dev.edu.ngochandev.productservice.dto.res.CategoryResponseDto;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
import jakarta.validation.Valid;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryRequestDto req);

    PageResponseDto<CategoryResponseDto> getAllCategories();

    void updateCategoryStatus(String id, boolean active);

    CategoryResponseDto updateCategory(@Valid UpdateCategoryRequestDto req);
}
