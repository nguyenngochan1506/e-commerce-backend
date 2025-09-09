package dev.edu.ngochandev.productservice.controller;

import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductDetailResponse;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductVariantResponseDto;
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

    @PostMapping("/{id}/variations")
    @ResponseStatus(HttpStatus.CREATED)
    public SuccessResponseDto<ProductVariantResponseDto> createProductVariant(
            @PathVariable("id") String productId,
            @RequestBody @Valid CreateProductVariantRequestDto req) {
        return SuccessResponseDto.<ProductVariantResponseDto>builder()
                .httpStatus(HttpStatus.CREATED)
                .message(translator.translate("product.variant.create.success"))
                .data(productService.createProductVariant(productId, req))
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

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<ProductDetailResponse> getProductDetail(@PathVariable("id") String productId) {
        return SuccessResponseDto.<ProductDetailResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("product.detail.success"))
                .data(productService.getProductDetail(productId))
                .build();
    }
}
