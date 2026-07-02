package com.beanteacher.deepfine.product.service;

import com.beanteacher.deepfine.product.dto.ProductDto;
import com.beanteacher.deepfine.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto.Response getById(Long id) {
        return productRepository.findById(id)
                .map(ProductDto.Response::from)
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + id));
    }

    public List<ProductDto.Response> getAll() {
        return productRepository.findAll().stream()
                .map(ProductDto.Response::from)
                .toList();
    }
}
