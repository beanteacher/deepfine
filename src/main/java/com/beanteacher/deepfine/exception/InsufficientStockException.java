package com.beanteacher.deepfine.exception;

public class InsufficientStockException extends RuntimeException {

    public InsufficientStockException(int current, int requested) {
        super(String.format("재고 부족: 현재 %d개, 요청 %d개", current, requested));
    }
}
