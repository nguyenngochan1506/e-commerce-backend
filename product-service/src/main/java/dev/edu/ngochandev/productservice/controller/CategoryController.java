package dev.edu.ngochandev.productservice.controller;

import dev.edu.ngochandev.productservice.dto.req.CreateCategoryRequestDto;
import dev.edu.ngochandev.productservice.dto.res.CategoryResponseDto;
import dev.edu.ngochandev.productservice.service.CategoryService;
import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final Translator translator;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SuccessResponseDto<CategoryResponseDto> createCate(@RequestBody @Valid CreateCategoryRequestDto req){
        return SuccessResponseDto.<CategoryResponseDto>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(translator.translate("category.create.success"))
                .data(categoryService.createCategory(req))
                .build();
    }
}
