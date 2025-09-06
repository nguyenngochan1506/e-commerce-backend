package dev.edu.ngochandev.productservice.dto.res;

import lombok.*;

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
}
