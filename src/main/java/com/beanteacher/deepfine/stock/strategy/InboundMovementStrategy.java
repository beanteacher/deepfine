package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.inventory.domain.Inventory;
import com.beanteacher.deepfine.stock.domain.MovementType;
import org.springframework.stereotype.Component;

@Component
public class InboundMovementStrategy implements StockMovementStrategy {

    @Override
    public MovementType getMovementType() {
        return MovementType.INBOUND;
    }

    @Override
    public void process(Inventory inventory, int quantity) {
        inventory.increaseQuantity(quantity);
    }
}
