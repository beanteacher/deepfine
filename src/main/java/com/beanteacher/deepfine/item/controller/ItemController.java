package com.beanteacher.deepfine.item.controller;

import com.beanteacher.deepfine.dto.ApiResponse;
import com.beanteacher.deepfine.item.dto.ItemDto;
import com.beanteacher.deepfine.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemDto.Response>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(itemService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ItemDto.Response>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(itemService.getById(id)));
    }
}
