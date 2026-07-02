package com.beanteacher.deepfine.site.controller;

import com.beanteacher.deepfine.dto.ApiResponse;
import com.beanteacher.deepfine.site.dto.SiteDto;
import com.beanteacher.deepfine.site.service.SiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sites")
@RequiredArgsConstructor
public class SiteController {

    private final SiteService siteService;

    @PostMapping
    public ResponseEntity<ApiResponse<SiteDto.Response>> create(@RequestBody @Valid SiteDto.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(siteService.create(request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SiteDto.Response>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(siteService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SiteDto.Response>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(siteService.getById(id)));
    }
}
