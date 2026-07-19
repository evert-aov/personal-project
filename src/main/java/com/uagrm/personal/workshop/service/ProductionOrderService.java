package com.uagrm.personal.workshop.service;

import com.uagrm.personal.workshop.dto.FrameDeliveryRequestDto;
import com.uagrm.personal.workshop.dto.FrameDeliveryResponseDto;
import com.uagrm.personal.workshop.dto.FrameTypePendingDeliveryDto;
import com.uagrm.personal.workshop.dto.MaterialUsageRequestDto;
import com.uagrm.personal.workshop.dto.MaterialUsageResponseDto;
import com.uagrm.personal.workshop.dto.OrderPendingDeliveryDto;
import com.uagrm.personal.workshop.dto.ProductionOrderRequestDto;
import com.uagrm.personal.workshop.dto.ProductionOrderResponseDto;
import com.uagrm.personal.workshop.entity.FrameDelivery;
import com.uagrm.personal.workshop.entity.Material;
import com.uagrm.personal.workshop.entity.MaterialUsage;
import com.uagrm.personal.workshop.entity.PictureFrame;
import com.uagrm.personal.workshop.entity.ProductionOrder;
import com.uagrm.personal.workshop.mapper.FrameDeliveryMapper;
import com.uagrm.personal.workshop.mapper.MaterialUsageMapper;
import com.uagrm.personal.workshop.mapper.ProductionOrderMapper;
import com.uagrm.personal.workshop.repository.FrameDeliveryRepository;
import com.uagrm.personal.workshop.repository.MaterialUsageRepository;
import com.uagrm.personal.workshop.repository.ProductionOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductionOrderService {
    private final ProductionOrderRepository productionOrderRepository;
    private final FrameDeliveryRepository frameDeliveryRepository;
    private final MaterialUsageRepository materialUsageRepository;
    private final ProductionOrderMapper productionOrderMapper;
    private final FrameDeliveryMapper frameDeliveryMapper;
    private final MaterialUsageMapper materialUsageMapper;
    private final PictureFrameService pictureFrameService;
    private final BatchRecipeService batchRecipeService;
    private final MaterialService materialService;

    @Transactional
    public ProductionOrderResponseDto createOrder(ProductionOrderRequestDto requestDto) {
        PictureFrame frame = pictureFrameService.findFrame(requestDto.frameId());

        ProductionOrder order = ProductionOrder.builder()
                .frame(frame)
                .recipe(batchRecipeService.findRecipe(requestDto.recipeId()))
                .requestedQuantity(requestDto.requestedQuantity())
                .startDate(requestDto.startDate())
                .endDate(requestDto.endDate())
                .status(requestDto.status() != null ? requestDto.status() : ProductionOrder.STATUS_IN_PROGRESS)
                .build();

        return productionOrderMapper.toDto(productionOrderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public Iterable<ProductionOrderResponseDto> getAllOrders() {
        return productionOrderRepository.findAll().stream().map(productionOrderMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProductionOrderResponseDto getOrderById(Long id) {
        return productionOrderMapper.toDto(findOrder(id));
    }

    @Transactional
    public ProductionOrderResponseDto updateOrder(Long id, ProductionOrderRequestDto requestDto) {
        ProductionOrder order = findOrder(id);
        order.setFrame(pictureFrameService.findFrame(requestDto.frameId()));
        order.setRecipe(batchRecipeService.findRecipe(requestDto.recipeId()));
        order.setRequestedQuantity(requestDto.requestedQuantity());
        order.setStartDate(requestDto.startDate());
        order.setEndDate(requestDto.endDate());
        if (requestDto.status() != null) {
            order.setStatus(requestDto.status());
        }
        return productionOrderMapper.toDto(productionOrderRepository.save(order));
    }

    @Transactional
    public void deleteOrder(Long id) {
        productionOrderRepository.deleteById(id);
    }

    @Transactional
    public FrameDeliveryResponseDto addDelivery(Long orderId, FrameDeliveryRequestDto requestDto) {
        ProductionOrder order = findOrder(orderId);

        FrameDelivery delivery = FrameDelivery.builder()
                .order(order)
                .deliveryDate(requestDto.deliveryDate())
                .quantityDelivered(requestDto.quantityDelivered())
                .remarks(requestDto.remarks())
                .build();

        return frameDeliveryMapper.toDto(frameDeliveryRepository.save(delivery));
    }

    @Transactional(readOnly = true)
    public Iterable<FrameDeliveryResponseDto> getDeliveries(Long orderId) {
        return frameDeliveryRepository.findAllByOrderId(orderId).stream().map(frameDeliveryMapper::toDto).toList();
    }

    @Transactional
    public void deleteDelivery(Long orderId, Long deliveryId) {
        frameDeliveryRepository.delete(findDelivery(orderId, deliveryId));
    }

    @Transactional
    public FrameDeliveryResponseDto updateDelivery(Long orderId, Long deliveryId, FrameDeliveryRequestDto requestDto) {
        FrameDelivery delivery = findDelivery(orderId, deliveryId);
        delivery.setDeliveryDate(requestDto.deliveryDate());
        delivery.setQuantityDelivered(requestDto.quantityDelivered());
        delivery.setRemarks(requestDto.remarks());
        return frameDeliveryMapper.toDto(frameDeliveryRepository.save(delivery));
    }

    /**
     * Replaces fn_stock_subtract_usage: validates there is enough stock and subtracts it
     * from the material, raising InsufficientStockException instead of failing at the DB level.
     */
    @Transactional
    public MaterialUsageResponseDto addMaterialUsage(Long orderId, MaterialUsageRequestDto requestDto) {
        ProductionOrder order = findOrder(orderId);
        Material material = materialService.findMaterial(requestDto.materialId());

        materialService.subtractStock(material.getId(), requestDto.quantityUsed());

        MaterialUsage usage = MaterialUsage.builder()
                .order(order)
                .material(material)
                .usageDate(requestDto.usageDate())
                .quantityUsed(requestDto.quantityUsed())
                .build();

        return materialUsageMapper.toDto(materialUsageRepository.save(usage));
    }

    @Transactional(readOnly = true)
    public Iterable<MaterialUsageResponseDto> getMaterialUsages(Long orderId) {
        return materialUsageRepository.findAllByOrderId(orderId).stream().map(materialUsageMapper::toDto).toList();
    }

    @Transactional
    public MaterialUsageResponseDto updateMaterialUsage(Long orderId, Long usageId, MaterialUsageRequestDto requestDto) {
        MaterialUsage usage = findMaterialUsage(orderId, usageId);

        BigDecimal quantityDelta = requestDto.quantityUsed().subtract(usage.getQuantityUsed());
        if (quantityDelta.compareTo(BigDecimal.ZERO) > 0) {
            materialService.subtractStock(usage.getMaterial().getId(), quantityDelta);
        } else if (quantityDelta.compareTo(BigDecimal.ZERO) < 0) {
            materialService.addStock(usage.getMaterial().getId(), quantityDelta.abs());
        }

        usage.setUsageDate(requestDto.usageDate());
        usage.setQuantityUsed(requestDto.quantityUsed());

        return materialUsageMapper.toDto(materialUsageRepository.save(usage));
    }

    @Transactional
    public void deleteMaterialUsage(Long orderId, Long usageId) {
        MaterialUsage usage = findMaterialUsage(orderId, usageId);
        materialService.addStock(usage.getMaterial().getId(), usage.getQuantityUsed());
        materialUsageRepository.delete(usage);
    }

    @Transactional(readOnly = true)
    public OrderPendingDeliveryDto getPendingByOrder(Long orderId) {
        ProductionOrder order = findOrder(orderId);
        int delivered = totalDelivered(orderId);
        int pending = order.getRequestedQuantity() - delivered;

        return new OrderPendingDeliveryDto(
                order.getId(),
                order.getFrame().getId(),
                order.getFrame().getDimensionDescription(),
                order.getRequestedQuantity(),
                delivered,
                pending
        );
    }

    @Transactional(readOnly = true)
    public List<FrameTypePendingDeliveryDto> getPendingByFrameType() {
        List<ProductionOrder> orders = productionOrderRepository.findAll();
        Map<Long, List<ProductionOrder>> ordersByFrame = orders.stream()
                .collect(Collectors.groupingBy(order -> order.getFrame().getId()));

        List<FrameTypePendingDeliveryDto> result = new ArrayList<>();
        for (List<ProductionOrder> frameOrders : ordersByFrame.values()) {
            PictureFrame frame = frameOrders.get(0).getFrame();
            int totalRequested = frameOrders.stream().mapToInt(ProductionOrder::getRequestedQuantity).sum();
            int totalDelivered = frameOrders.stream().mapToInt(order -> totalDelivered(order.getId())).sum();
            int totalPending = totalRequested - totalDelivered;

            result.add(new FrameTypePendingDeliveryDto(frame.getId(), frame.getDimensionDescription(),
                    totalRequested, totalDelivered, totalPending));
        }
        result.sort(Comparator.comparing(FrameTypePendingDeliveryDto::frameDescription));
        return result;
    }

    private int totalDelivered(Long orderId) {
        return frameDeliveryRepository.findAllByOrderId(orderId).stream()
                .mapToInt(FrameDelivery::getQuantityDelivered)
                .sum();
    }

    ProductionOrder findOrder(Long id) {
        return productionOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Production order not found"));
    }

    private FrameDelivery findDelivery(Long orderId, Long deliveryId) {
        FrameDelivery delivery = frameDeliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Frame delivery not found"));
        if (!delivery.getOrder().getId().equals(orderId)) {
            throw new RuntimeException("Frame delivery does not belong to order " + orderId);
        }
        return delivery;
    }

    private MaterialUsage findMaterialUsage(Long orderId, Long usageId) {
        MaterialUsage usage = materialUsageRepository.findById(usageId)
                .orElseThrow(() -> new RuntimeException("Material usage not found"));
        if (!usage.getOrder().getId().equals(orderId)) {
            throw new RuntimeException("Material usage does not belong to order " + orderId);
        }
        return usage;
    }
}
