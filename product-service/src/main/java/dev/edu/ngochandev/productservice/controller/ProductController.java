package dev.edu.ngochandev.productservice.controller;

import dev.edu.ngochandev.productservice.dto.req.CreateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.CreateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.req.UpdateProductRequestDto;
import dev.edu.ngochandev.productservice.dto.req.UpdateProductVariantRequestDto;
import dev.edu.ngochandev.productservice.dto.res.ProductDetailResponse;
import dev.edu.ngochandev.productservice.dto.res.ProductListResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductResponseDto;
import dev.edu.ngochandev.productservice.dto.res.ProductVariantResponseDto;
import dev.edu.ngochandev.productservice.service.ProductService;
import dev.edu.ngochandev.sharedkernel.common.Translator;
import dev.edu.ngochandev.sharedkernel.dto.res.PageResponseDto;
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


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)//public endpoint
    public SuccessResponseDto<ProductDetailResponse> getProductDetail(@PathVariable("id") String productId) {
        return SuccessResponseDto.<ProductDetailResponse>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("product.detail.success"))
                .data(productService.getProductDetail(productId))
                .build();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)//public endpoint
    public SuccessResponseDto<PageResponseDto<ProductListResponseDto>> getListProducts(
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "id:desc") String sort ,
            @RequestParam(value = "search", required = false, defaultValue = "") String search) {
        return SuccessResponseDto.<PageResponseDto<ProductListResponseDto>>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("product.list.success"))
                .data(productService.getListProducts(page, size, sort, search))
                .build();
    }

    @PostMapping("/{id}/variants")
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

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<ProductResponseDto> updateProduct(@RequestBody @Valid UpdateProductRequestDto req, @PathVariable("id") String productId) {
        return SuccessResponseDto.<ProductResponseDto>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("product.update.success"))
                .data(productService.updateProduct(productId, req))
                .build();
    }
    @PatchMapping("/{productId}/variants/{variantId}")
    @ResponseStatus(HttpStatus.OK)
    public SuccessResponseDto<ProductVariantResponseDto> updateProductVariant(@RequestBody @Valid UpdateProductVariantRequestDto req, @PathVariable("productId") String productId, @PathVariable("variantId") String variantId) {
        return SuccessResponseDto.<ProductVariantResponseDto>builder()
                .httpStatus(HttpStatus.OK)
                .message(translator.translate("product.variant.update.success"))
                .data(productService.updateProductVariant(productId, variantId, req))
                .build();
    }
}
