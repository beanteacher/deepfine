package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.product.domain.Product;
import com.beanteacher.deepfine.stock.domain.MovementType;
import org.springframework.stereotype.Component;

@Component
public class OutboundMovementStrategy implements StockMovementStrategy {

    @Override
    public MovementType getMovementType() {
        return MovementType.OUTBOUND;
    }

    @Override
    public void process(Product product, int quantity) {
        product.decreaseQuantity(quantity);  // 재고 부족 시 InsufficientStockException 발생
    }
}
