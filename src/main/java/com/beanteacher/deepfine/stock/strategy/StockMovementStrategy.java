package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.inventory.domain.Inventory;
import com.beanteacher.deepfine.stock.domain.MovementType;

public interface StockMovementStrategy {

    MovementType getMovementType();

    void process(Inventory inventory, int quantity);
}
