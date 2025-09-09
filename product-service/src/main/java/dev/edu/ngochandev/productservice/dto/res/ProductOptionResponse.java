package dev.edu.ngochandev.productservice.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ProductOptionResponse {
    private String id;
    private String name;
    private List<ProductOptionValueResponseDto> values;
}
