package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.product.domain.Product;
import com.beanteacher.deepfine.stock.domain.MovementType;
import org.springframework.stereotype.Component;

/**
 * 재고 조정 전략 — OCP 확장 예시
 * 기존 코드(StockMovementStrategy, StockMovementStrategyFactory) 수정 없이
 * @Component 등록만으로 팩토리에 자동 주입된다.
 */
@Component
public class AdjustmentMovementStrategy implements StockMovementStrategy {

    @Override
    public MovementType getMovementType() {
        return MovementType.ADJUSTMENT;
    }

    @Override
    public void process(Product product, int quantity) {
        product.adjustQuantity(quantity);  // 증감이 아닌 목표값으로 직접 설정
    }
}
