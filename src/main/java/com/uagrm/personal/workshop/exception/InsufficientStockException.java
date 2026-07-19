package com.uagrm.personal.workshop.exception;

import java.math.BigDecimal;

/**
 * Se lanza cuando se intenta registrar un consumo (u otro descuento) de material
 * mayor al stock actualmente disponible. Reemplaza la validación que antes
 * hacía el trigger fn_stock_subtract_usage en PostgreSQL.
 */
public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long materialId, BigDecimal available, BigDecimal requested) {
        super("Insufficient stock for material %d: available %s, requested %s"
                .formatted(materialId, available, requested));
    }
}
