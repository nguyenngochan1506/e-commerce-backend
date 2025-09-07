package dev.edu.ngochandev.productservice.controller;

import dev.edu.ngochandev.productservice.dto.req.CreateProductOptionRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductOptionResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.service.ProductService;
import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final Translator translator;

    @PostMapping("/options")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<ProductOptionResponseDto> createProductOption(@RequestBody CreateProductOptionRequestDto req) {
        return SuccessResponseDto.<ProductOptionResponseDto>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(translator.translate("product.option.create.success"))
                .data(productService.createProductOption(req))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<ProductResponseDto> createProduct(@RequestBody @Valid CreateProductRequestDto req) {
        return SuccessResponseDto.<ProductResponseDto>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("product.create.success"))
                .data(productService.createProduct(req))
                .build();
    }
}
