package com.beanteacher.deepfine.stock.strategy;

import com.beanteacher.deepfine.stock.domain.MovementType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 이동 유형에 맞는 전략을 반환하는 팩토리 (DIP)
 * Spring이 StockMovementStrategy 구현체를 자동 주입한다.
 */
@Component
public class StockMovementStrategyFactory {

    private final Map<MovementType, StockMovementStrategy> strategies;

    public StockMovementStrategyFactory(List<StockMovementStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(StockMovementStrategy::getMovementType, s -> s));
    }

    public StockMovementStrategy getStrategy(MovementType type) {
        return Optional.ofNullable(strategies.get(type))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 이동 유형: " + type));
    }
}
