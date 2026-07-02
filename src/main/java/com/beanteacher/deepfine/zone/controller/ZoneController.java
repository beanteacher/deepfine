package com.beanteacher.deepfine.zone.controller;

import com.beanteacher.deepfine.dto.ApiResponse;
import com.beanteacher.deepfine.zone.dto.ZoneDto;
import com.beanteacher.deepfine.zone.service.ZoneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/zones")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    public ResponseEntity<ApiResponse<ZoneDto.Response>> create(@RequestBody @Valid ZoneDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(zoneService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ZoneDto.Response>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(zoneService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneDto.Response>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(zoneService.getById(id)));
    }
}
