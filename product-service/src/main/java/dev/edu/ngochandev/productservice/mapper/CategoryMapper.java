package dev.edu.ngochandev.productservice.mapper;

import dev.edu.ngochandev.productservice.dto.res.CategoryResponseDto;
import dev.edu.ngochandev.productservice.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "children", source = "children", qualifiedByName = "recursiveCategoryMapping")
    @Named("recursiveCategoryMapping")
    CategoryResponseDto toCategoryResponseDto(CategoryEntity categoryEntity);

    @Mapping(target = "parentId", source = "parent.id")
    @Mapping(target = "children", ignore = true)
    @Named("flatCategoryMapping")
    CategoryResponseDto toFlatCategoryResponseDto(CategoryEntity categoryEntity);
}
