package com.beanteacher.deepfine.stock.service;

import com.beanteacher.deepfine.inventory.domain.Inventory;
import com.beanteacher.deepfine.inventory.repository.InventoryRepository;
import com.beanteacher.deepfine.item.domain.Item;
import com.beanteacher.deepfine.item.repository.ItemRepository;
import com.beanteacher.deepfine.stock.domain.MovementType;
import com.beanteacher.deepfine.stock.domain.StockMovement;
import com.beanteacher.deepfine.stock.dto.StockDto;
import com.beanteacher.deepfine.stock.repository.StockMovementRepository;
import com.beanteacher.deepfine.stock.strategy.StockMovementStrategyFactory;
import com.beanteacher.deepfine.zone.domain.Zone;
import com.beanteacher.deepfine.zone.repository.ZoneRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StockService {

    private final ItemRepository itemRepository;
    private final ZoneRepository zoneRepository;
    private final InventoryRepository inventoryRepository;
    private final StockMovementRepository stockMovementRepository;
    private final StockMovementStrategyFactory strategyFactory;

    public StockDto.Response inbound(StockDto.InboundRequest request) {
        Item item = itemRepository.findByNameWithLock(request.itemName())
                .orElseGet(() -> itemRepository.save(Item.create(request.itemName())));

        Zone zone = zoneRepository.findById(request.zoneId())
                .orElseThrow(() -> new EntityNotFoundException("구역을 찾을 수 없습니다: " + request.zoneId()));

        Inventory inventory = inventoryRepository
                .findByItemIdAndZoneIdWithLock(item.getId(), zone.getId())
                .orElseGet(() -> inventoryRepository.save(Inventory.create(item, zone)));

        strategyFactory.getStrategy(MovementType.INBOUND).process(inventory, request.quantity());
        stockMovementRepository.save(StockMovement.create(item, zone, MovementType.INBOUND, request.quantity()));

        return StockDto.Response.from(inventory);
    }

    public StockDto.Response outbound(StockDto.OutboundRequest request) {
        Inventory inventory = inventoryRepository
                .findByItemIdAndZoneIdWithLock(request.itemId(), request.zoneId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "해당 구역에 품목 재고가 없습니다. 품목 ID: " + request.itemId() + ", 구역 ID: " + request.zoneId()));

        strategyFactory.getStrategy(MovementType.OUTBOUND).process(inventory, request.quantity());
        stockMovementRepository.save(StockMovement.create(
                inventory.getItem(), inventory.getZone(), MovementType.OUTBOUND, request.quantity()));

        return StockDto.Response.from(inventory);
    }

    @Transactional(readOnly = true)
    public List<StockDto.Response> getStock(Long itemId) {
        itemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다: " + itemId));
        return inventoryRepository.findByItemIdWithZoneAndSite(itemId)
                .stream().map(StockDto.Response::from).toList();
    }

    public StockDto.Response adjust(StockDto.AdjustmentRequest request) {
        Inventory inventory = inventoryRepository
                .findByItemIdAndZoneIdWithLock(request.itemId(), request.zoneId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "해당 구역에 품목 재고가 없습니다. 품목 ID: " + request.itemId() + ", 구역 ID: " + request.zoneId()));

        strategyFactory.getStrategy(MovementType.ADJUSTMENT).process(inventory, request.targetQuantity());
        stockMovementRepository.save(StockMovement.create(
                inventory.getItem(), inventory.getZone(), MovementType.ADJUSTMENT, request.targetQuantity()));

        return StockDto.Response.from(inventory);
    }
}
