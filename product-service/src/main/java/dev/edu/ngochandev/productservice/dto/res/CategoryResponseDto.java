package dev.edu.ngochandev.productservice.dto.res;

import lombok.*;

import java.util.List;

// response for normal user
@Builder
@Getter
public class CategoryResponseDto {
    private String id;
    private String name;
    private String description;
    private String slug;
    private String thumbnail;
    private String parentId;
    private Integer level;
    @Builder.Default
    private List<CategoryResponseDto> children = List.of();
}
