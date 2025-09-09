package dev.edu.ngochandev.productservice.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryRequestDto extends CreateCategoryRequestDto{
    private String id;
}
