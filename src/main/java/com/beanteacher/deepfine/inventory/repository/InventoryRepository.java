package com.beanteacher.deepfine.inventory.repository;

import com.beanteacher.deepfine.inventory.domain.Inventory;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM Inventory i WHERE i.item.id = :itemId AND i.zone.id = :zoneId")
    Optional<Inventory> findByItemIdAndZoneIdWithLock(@Param("itemId") Long itemId, @Param("zoneId") Long zoneId);

    @Query("SELECT i FROM Inventory i JOIN FETCH i.zone z JOIN FETCH z.site WHERE i.item.id = :itemId")
    List<Inventory> findByItemIdWithZoneAndSite(@Param("itemId") Long itemId);
}
