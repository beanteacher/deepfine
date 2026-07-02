package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.product.domain.Product;
import com.beanteacher.deepfine.stock.domain.MovementType;

/**
 * 재고 이동 전략 인터페이스 (OCP)
 * 새로운 이동 유형(TRANSFER, ADJUSTMENT 등)은 이 인터페이스를 구현하는 방식으로 확장한다.
 */
public interface StockMovementStrategy {

    MovementType getMovementType();

    void process(Product product, int quantity);
}
