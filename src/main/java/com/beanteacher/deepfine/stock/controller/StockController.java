package com.beanteacher.deepfine.stock.controller;

import com.beanteacher.deepfine.dto.ApiResponse;
import com.beanteacher.deepfine.stock.dto.StockDto;
import com.beanteacher.deepfine.stock.service.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/inbound")
    public ResponseEntity<ApiResponse<StockDto.Response>> inbound(@RequestBody @Valid StockDto.InboundRequest request) {
        return ResponseEntity.ok(ApiResponse.success(stockService.inbound(request)));
    }

    @PostMapping("/outbound")
    public ResponseEntity<ApiResponse<StockDto.Response>> outbound(@RequestBody @Valid StockDto.OutboundRequest request) {
        return ResponseEntity.ok(ApiResponse.success(stockService.outbound(request)));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ApiResponse<List<StockDto.Response>>> getStock(@PathVariable Long itemId) {
        return ResponseEntity.ok(ApiResponse.success(stockService.getStock(itemId)));
    }

    @PostMapping("/adjustment")
    public ResponseEntity<ApiResponse<StockDto.Response>> adjust(@RequestBody @Valid StockDto.AdjustmentRequest request) {
        return ResponseEntity.ok(ApiResponse.success(stockService.adjust(request)));
    }
}
