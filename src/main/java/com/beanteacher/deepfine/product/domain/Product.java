package com.beanteacher.deepfine.product.domain;

import com.beanteacher.deepfine.exception.InsufficientStockException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Product create(String name) {
        Product product = new Product();
        product.name = name;
        product.quantity = 0;
        return product;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity < amount) {
            throw new InsufficientStockException(this.quantity, amount);
        }
        this.quantity -= amount;
    }

    public void adjustQuantity(int targetQuantity) {
        if (targetQuantity < 0) {
            throw new IllegalArgumentException("조정 수량은 0 이상이어야 합니다.");
        }
        this.quantity = targetQuantity;
    }
}
