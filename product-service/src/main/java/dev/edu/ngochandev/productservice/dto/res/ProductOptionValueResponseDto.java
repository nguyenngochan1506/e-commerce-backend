package dev.edu.ngochandev.productservice.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductOptionValueResponseDto {
    private String id;
    private String value;
}
