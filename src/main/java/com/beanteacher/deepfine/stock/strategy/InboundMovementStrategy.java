package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.product.domain.Product;
import com.beanteacher.deepfine.stock.domain.MovementType;
import org.springframework.stereotype.Component;

@Component
public class InboundMovementStrategy implements StockMovementStrategy {

    @Override
    public MovementType getMovementType() {
        return MovementType.INBOUND;
    }

    @Override
    public void process(Product product, int quantity) {
        product.increaseQuantity(quantity);
    }
}
