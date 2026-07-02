package com.beanteacher.deepfine.inventory.domain;

import com.beanteacher.deepfine.exception.InsufficientStockException;
import com.beanteacher.deepfine.item.domain.Item;
import com.beanteacher.deepfine.zone.domain.Zone;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventories",
        uniqueConstraints = @UniqueConstraint(columnNames = {"item_id", "zone_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    private int quantity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Inventory create(Item item, Zone zone) {
        Inventory inventory = new Inventory();
        inventory.item = item;
        inventory.zone = zone;
        inventory.quantity = 0;
        return inventory;
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
