package dev.edu.ngochandev.productservice.service;

import dev.edu.ngochandev.productservice.dto.req.CreateCategoryRequestDto;
import dev.edu.ngochandev.productservice.dto.res.CategoryResponseDto;

public interface CategoryService {
    CategoryResponseDto createCategory(CreateCategoryRequestDto req);
}
