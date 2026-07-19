package com.uagrm.personal.workshop.controller;

import com.uagrm.personal.workshop.dto.FrameDeliveryRequestDto;
import com.uagrm.personal.workshop.dto.FrameDeliveryResponseDto;
import com.uagrm.personal.workshop.dto.FrameTypePendingDeliveryDto;
import com.uagrm.personal.workshop.dto.MaterialUsageRequestDto;
import com.uagrm.personal.workshop.dto.MaterialUsageResponseDto;
import com.uagrm.personal.workshop.dto.OrderPendingDeliveryDto;
import com.uagrm.personal.workshop.dto.ProductionOrderRequestDto;
import com.uagrm.personal.workshop.dto.ProductionOrderResponseDto;
import com.uagrm.personal.workshop.service.ProductionOrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workshop/orders")
@RequiredArgsConstructor
@Tag(name = "Workshop - Production Orders", description = "Órdenes de producción, entregas y consumo de material")
public class ProductionOrderController {
    private final ProductionOrderService productionOrderService;

    @PostMapping
    public ResponseEntity<ProductionOrderResponseDto> createOrder(@RequestBody ProductionOrderRequestDto requestDto) {
        return new ResponseEntity<>(productionOrderService.createOrder(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<ProductionOrderResponseDto>> getAllOrders() {
        return ResponseEntity.ok(productionOrderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionOrderResponseDto> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(productionOrderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductionOrderResponseDto> updateOrder(@PathVariable Long id, @RequestBody ProductionOrderRequestDto requestDto) {
        return ResponseEntity.ok(productionOrderService.updateOrder(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        productionOrderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{orderId}/deliveries")
    public ResponseEntity<FrameDeliveryResponseDto> addDelivery(@PathVariable Long orderId,
                                                                   @RequestBody FrameDeliveryRequestDto requestDto) {
        return new ResponseEntity<>(productionOrderService.addDelivery(orderId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}/deliveries")
    public ResponseEntity<Iterable<FrameDeliveryResponseDto>> getDeliveries(@PathVariable Long orderId) {
        return ResponseEntity.ok(productionOrderService.getDeliveries(orderId));
    }

    @PutMapping("/{orderId}/deliveries/{deliveryId}")
    public ResponseEntity<FrameDeliveryResponseDto> updateDelivery(@PathVariable Long orderId,
                                                                      @PathVariable Long deliveryId,
                                                                      @RequestBody FrameDeliveryRequestDto requestDto) {
        return ResponseEntity.ok(productionOrderService.updateDelivery(orderId, deliveryId, requestDto));
    }

    @DeleteMapping("/{orderId}/deliveries/{deliveryId}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long orderId, @PathVariable Long deliveryId) {
        productionOrderService.deleteDelivery(orderId, deliveryId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Registers material consumption: validates available stock and subtracts it
     * (throws InsufficientStockException instead of relying on a DB trigger).
     */
    @PostMapping("/{orderId}/material-usages")
    public ResponseEntity<MaterialUsageResponseDto> addMaterialUsage(@PathVariable Long orderId,
                                                                        @RequestBody MaterialUsageRequestDto requestDto) {
        return new ResponseEntity<>(productionOrderService.addMaterialUsage(orderId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}/material-usages")
    public ResponseEntity<Iterable<MaterialUsageResponseDto>> getMaterialUsages(@PathVariable Long orderId) {
        return ResponseEntity.ok(productionOrderService.getMaterialUsages(orderId));
    }

    @PutMapping("/{orderId}/material-usages/{usageId}")
    public ResponseEntity<MaterialUsageResponseDto> updateMaterialUsage(@PathVariable Long orderId,
                                                                           @PathVariable Long usageId,
                                                                           @RequestBody MaterialUsageRequestDto requestDto) {
        return ResponseEntity.ok(productionOrderService.updateMaterialUsage(orderId, usageId, requestDto));
    }

    @DeleteMapping("/{orderId}/material-usages/{usageId}")
    public ResponseEntity<Void> deleteMaterialUsage(@PathVariable Long orderId, @PathVariable Long usageId) {
        productionOrderService.deleteMaterialUsage(orderId, usageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{orderId}/pending")
    public ResponseEntity<OrderPendingDeliveryDto> getPendingByOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(productionOrderService.getPendingByOrder(orderId));
    }

    @GetMapping("/pending-by-frame-type")
    public ResponseEntity<List<FrameTypePendingDeliveryDto>> getPendingByFrameType() {
        return ResponseEntity.ok(productionOrderService.getPendingByFrameType());
    }
}
