package dev.edu.ngochandev.productservice.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CreateProductOptionRequestDto {
    @NotBlank(message = "error.name.not-blank")
    private String name;

    @NotNull(message = "error.values.not-null")
    @NotEmpty(message = "error.values.not-empty")
    private List<String> values;
}
