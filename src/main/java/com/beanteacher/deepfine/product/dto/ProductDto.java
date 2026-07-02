package com.beanteacher.deepfine.product.dto;

import com.beanteacher.deepfine.product.domain.Product;

public class ProductDto {

    public record Response(
            Long id,
            String name,
            int quantity
    ) {
        public static Response from(Product product) {
            return new Response(product.getId(), product.getName(), product.getQuantity());
        }
    }
}
