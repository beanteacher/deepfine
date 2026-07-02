package com.beanteacher.deepfine.stock.repository;

import com.beanteacher.deepfine.stock.domain.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
}
