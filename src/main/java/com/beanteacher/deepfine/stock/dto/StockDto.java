package com.beanteacher.deepfine.stock.dto;

import com.beanteacher.deepfine.product.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class StockDto {

    public record InboundRequest(
            @NotBlank(message = "상품명은 필수입니다") String productName,
            @Positive(message = "수량은 0보다 커야 합니다") int quantity
    ) {}

    public record OutboundRequest(
            @NotNull(message = "상품 ID는 필수입니다") Long productId,
            @Positive(message = "수량은 0보다 커야 합니다") int quantity
    ) {}

    public record AdjustmentRequest(
            @NotNull(message = "상품 ID는 필수입니다") Long productId,
            @Min(value = 0, message = "조정 수량은 0 이상이어야 합니다") int targetQuantity
    ) {}

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
