package com.uagrm.personal.workshop.service;

import com.uagrm.personal.workshop.dto.PurchaseDetailRequestDto;
import com.uagrm.personal.workshop.dto.PurchaseDetailResponseDto;
import com.uagrm.personal.workshop.dto.PurchaseRequestDto;
import com.uagrm.personal.workshop.dto.PurchaseResponseDto;
import com.uagrm.personal.workshop.entity.Material;
import com.uagrm.personal.workshop.entity.Purchase;
import com.uagrm.personal.workshop.entity.PurchaseDetail;
import com.uagrm.personal.workshop.mapper.PurchaseDetailMapper;
import com.uagrm.personal.workshop.mapper.PurchaseMapper;
import com.uagrm.personal.workshop.repository.PurchaseDetailRepository;
import com.uagrm.personal.workshop.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    private final PurchaseDetailRepository purchaseDetailRepository;
    private final PurchaseMapper purchaseMapper;
    private final PurchaseDetailMapper purchaseDetailMapper;
    private final MaterialService materialService;

    @Transactional
    public PurchaseResponseDto createPurchase(PurchaseRequestDto requestDto) {
        Purchase purchase = Purchase.builder()
                .purchaseDate(requestDto.purchaseDate())
                .supplier(requestDto.supplier())
                .total(BigDecimal.ZERO)
                .build();

        return purchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    @Transactional(readOnly = true)
    public Iterable<PurchaseResponseDto> getAllPurchases() {
        return purchaseRepository.findAll().stream().map(purchaseMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public PurchaseResponseDto getPurchaseById(Long id) {
        return purchaseMapper.toDto(findPurchase(id));
    }

    @Transactional
    public PurchaseResponseDto updatePurchase(Long id, PurchaseRequestDto requestDto) {
        Purchase purchase = findPurchase(id);
        purchase.setPurchaseDate(requestDto.purchaseDate());
        purchase.setSupplier(requestDto.supplier());
        return purchaseMapper.toDto(purchaseRepository.save(purchase));
    }

    @Transactional
    public void deletePurchase(Long id) {
        purchaseRepository.deleteById(id);
    }

    /**
     * Replaces fn_stock_add_purchase + trg_recalculate_purchase_total:
     * adds the purchased quantity to the material's stock and recomputes the purchase total,
     * all inside the same transaction as the insert.
     */
    @Transactional
    public PurchaseDetailResponseDto addPurchaseDetail(Long purchaseId, PurchaseDetailRequestDto requestDto) {
        Purchase purchase = findPurchase(purchaseId);
        Material material = materialService.findMaterial(requestDto.materialId());

        PurchaseDetail detail = PurchaseDetail.builder()
                .purchase(purchase)
                .material(material)
                .quantityPurchased(requestDto.quantityPurchased())
                .unitPrice(requestDto.unitPrice())
                .subtotal(requestDto.quantityPurchased().multiply(requestDto.unitPrice()))
                .build();
        detail = purchaseDetailRepository.save(detail);

        materialService.addStock(material.getId(), requestDto.quantityPurchased());
        recalculateTotal(purchaseId);

        return purchaseDetailMapper.toDto(detail);
    }

    @Transactional(readOnly = true)
    public Iterable<PurchaseDetailResponseDto> getPurchaseDetails(Long purchaseId) {
        return purchaseDetailRepository.findAllByPurchaseId(purchaseId).stream()
                .map(purchaseDetailMapper::toDto).toList();
    }

    @Transactional
    public PurchaseDetailResponseDto updatePurchaseDetail(Long purchaseId, Long detailId, PurchaseDetailRequestDto requestDto) {
        PurchaseDetail detail = findPurchaseDetail(purchaseId, detailId);

        BigDecimal quantityDelta = requestDto.quantityPurchased().subtract(detail.getQuantityPurchased());
        if (quantityDelta.compareTo(BigDecimal.ZERO) > 0) {
            materialService.addStock(detail.getMaterial().getId(), quantityDelta);
        } else if (quantityDelta.compareTo(BigDecimal.ZERO) < 0) {
            materialService.subtractStock(detail.getMaterial().getId(), quantityDelta.abs());
        }

        detail.setQuantityPurchased(requestDto.quantityPurchased());
        detail.setUnitPrice(requestDto.unitPrice());
        detail.setSubtotal(requestDto.quantityPurchased().multiply(requestDto.unitPrice()));
        detail = purchaseDetailRepository.save(detail);

        recalculateTotal(purchaseId);

        return purchaseDetailMapper.toDto(detail);
    }

    @Transactional
    public void deletePurchaseDetail(Long purchaseId, Long detailId) {
        PurchaseDetail detail = findPurchaseDetail(purchaseId, detailId);
        materialService.subtractStock(detail.getMaterial().getId(), detail.getQuantityPurchased());
        purchaseDetailRepository.delete(detail);
        recalculateTotal(purchaseId);
    }

    private void recalculateTotal(Long purchaseId) {
        List<PurchaseDetail> details = purchaseDetailRepository.findAllByPurchaseId(purchaseId);
        BigDecimal total = details.stream()
                .map(PurchaseDetail::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Purchase purchase = findPurchase(purchaseId);
        purchase.setTotal(total);
        purchaseRepository.save(purchase);
    }

    Purchase findPurchase(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));
    }

    private PurchaseDetail findPurchaseDetail(Long purchaseId, Long detailId) {
        PurchaseDetail detail = purchaseDetailRepository.findById(detailId)
                .orElseThrow(() -> new RuntimeException("Purchase detail not found"));
        if (!detail.getPurchase().getId().equals(purchaseId)) {
            throw new RuntimeException("Purchase detail does not belong to purchase " + purchaseId);
        }
        return detail;
    }
}
