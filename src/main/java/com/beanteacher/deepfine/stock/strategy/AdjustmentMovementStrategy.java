package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.inventory.domain.Inventory;
import com.beanteacher.deepfine.stock.domain.MovementType;
import org.springframework.stereotype.Component;

@Component
public class AdjustmentMovementStrategy implements StockMovementStrategy {

    @Override
    public MovementType getMovementType() {
        return MovementType.ADJUSTMENT;
    }

    @Override
    public void process(Inventory inventory, int quantity) {
        inventory.adjustQuantity(quantity);
    }
}
