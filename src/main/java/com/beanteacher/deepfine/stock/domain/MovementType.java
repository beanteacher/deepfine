package com.beanteacher.deepfine.stock.domain;

public enum MovementType {
    INBOUND,    // 입고
    OUTBOUND,   // 출고
    ADJUSTMENT  // 재고 조정 (실사 후 목표 수량으로 직접 설정)
}
