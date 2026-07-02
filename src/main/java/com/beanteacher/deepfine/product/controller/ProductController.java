package com.beanteacher.deepfine.product.controller;

import com.beanteacher.deepfine.dto.ApiResponse;
import com.beanteacher.deepfine.product.dto.ProductDto;
import com.beanteacher.deepfine.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto.Response>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto.Response>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getById(id)));
    }
}
