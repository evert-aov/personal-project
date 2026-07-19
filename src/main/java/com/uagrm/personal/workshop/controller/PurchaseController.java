package com.uagrm.personal.workshop.controller;

import com.uagrm.personal.workshop.dto.PurchaseDetailRequestDto;
import com.uagrm.personal.workshop.dto.PurchaseDetailResponseDto;
import com.uagrm.personal.workshop.dto.PurchaseRequestDto;
import com.uagrm.personal.workshop.dto.PurchaseResponseDto;
import com.uagrm.personal.workshop.service.PurchaseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workshop/purchases")
@RequiredArgsConstructor
@Tag(name = "Workshop - Purchases", description = "Compras y su detalle (compras, detalle_compra)")
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponseDto> createPurchase(@RequestBody PurchaseRequestDto requestDto) {
        return new ResponseEntity<>(purchaseService.createPurchase(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Iterable<PurchaseResponseDto>> getAllPurchases() {
        return ResponseEntity.ok(purchaseService.getAllPurchases());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponseDto> getPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseResponseDto> updatePurchase(@PathVariable Long id, @RequestBody PurchaseRequestDto requestDto) {
        return ResponseEntity.ok(purchaseService.updatePurchase(id, requestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        purchaseService.deletePurchase(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Registers a purchase detail: adds the purchased quantity to the material's stock
     * and recalculates the purchase total (replaces the PostgreSQL triggers).
     */
    @PostMapping("/{purchaseId}/details")
    public ResponseEntity<PurchaseDetailResponseDto> addPurchaseDetail(@PathVariable Long purchaseId,
                                                                         @RequestBody PurchaseDetailRequestDto requestDto) {
        return new ResponseEntity<>(purchaseService.addPurchaseDetail(purchaseId, requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/{purchaseId}/details")
    public ResponseEntity<Iterable<PurchaseDetailResponseDto>> getPurchaseDetails(@PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseService.getPurchaseDetails(purchaseId));
    }

    @PutMapping("/{purchaseId}/details/{detailId}")
    public ResponseEntity<PurchaseDetailResponseDto> updatePurchaseDetail(@PathVariable Long purchaseId,
                                                                            @PathVariable Long detailId,
                                                                            @RequestBody PurchaseDetailRequestDto requestDto) {
        return ResponseEntity.ok(purchaseService.updatePurchaseDetail(purchaseId, detailId, requestDto));
    }

    @DeleteMapping("/{purchaseId}/details/{detailId}")
    public ResponseEntity<Void> deletePurchaseDetail(@PathVariable Long purchaseId, @PathVariable Long detailId) {
        purchaseService.deletePurchaseDetail(purchaseId, detailId);
        return ResponseEntity.noContent().build();
    }
}
