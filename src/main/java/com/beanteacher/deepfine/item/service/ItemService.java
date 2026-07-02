package com.beanteacher.deepfine.item.service;

import com.beanteacher.deepfine.inventory.repository.InventoryRepository;
import com.beanteacher.deepfine.item.dto.ItemDto;
import com.beanteacher.deepfine.item.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    public ItemDto.Response getById(Long id) {
        return itemRepository.findById(id)
                .map(item -> ItemDto.Response.from(item, inventoryRepository.findByItemIdWithZoneAndSite(id)))
                .orElseThrow(() -> new EntityNotFoundException("품목을 찾을 수 없습니다: " + id));
    }

    public List<ItemDto.Response> getAll() {
        return itemRepository.findAll().stream()
                .map(item -> ItemDto.Response.from(item, inventoryRepository.findByItemIdWithZoneAndSite(item.getId())))
                .toList();
    }
}
