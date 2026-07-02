package com.beanteacher.deepfine.stock.service;

import com.beanteacher.deepfine.product.domain.Product;
import com.beanteacher.deepfine.product.repository.ProductRepository;
import com.beanteacher.deepfine.stock.domain.MovementType;
import com.beanteacher.deepfine.stock.domain.StockMovement;
import com.beanteacher.deepfine.stock.dto.StockDto;
import com.beanteacher.deepfine.stock.repository.StockMovementRepository;
import com.beanteacher.deepfine.stock.strategy.StockMovementStrategyFactory;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final ProductRepository productRepository;
    private final StockMovementRepository stockMovementRepository;
    private final StockMovementStrategyFactory strategyFactory;

    /**
     * 입고 처리
     * 미등록 상품이면 신규 등록 후 입고, PESSIMISTIC_WRITE 락으로 동시성 제어
     */
    public StockDto.Response inbound(StockDto.InboundRequest request) {
        Product product = productRepository.findByNameWithLock(request.productName())
                .orElseGet(() -> productRepository.save(Product.create(request.productName())));

        strategyFactory.getStrategy(MovementType.INBOUND).process(product, request.quantity());
        stockMovementRepository.save(StockMovement.create(product, MovementType.INBOUND, request.quantity()));

        return StockDto.Response.from(product);
    }

    /**
     * 출고 처리
     * 재고 부족 시 InsufficientStockException, PESSIMISTIC_WRITE 락으로 동시성 제어
     */
    public StockDto.Response outbound(StockDto.OutboundRequest request) {
        Product product = productRepository.findByIdWithLock(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + request.productId()));

        strategyFactory.getStrategy(MovementType.OUTBOUND).process(product, request.quantity());
        stockMovementRepository.save(StockMovement.create(product, MovementType.OUTBOUND, request.quantity()));

        return StockDto.Response.from(product);
    }

    /**
     * 재고 조정 처리
     * 실사 후 시스템 수량을 실물 수량으로 직접 맞출 때 사용
     */
    public StockDto.Response adjust(StockDto.AdjustmentRequest request) {
        Product product = productRepository.findByIdWithLock(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다: " + request.productId()));

        strategyFactory.getStrategy(MovementType.ADJUSTMENT).process(product, request.targetQuantity());
        stockMovementRepository.save(StockMovement.create(product, MovementType.ADJUSTMENT, request.targetQuantity()));

        return StockDto.Response.from(product);
    }
}
