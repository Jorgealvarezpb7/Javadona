package com.supermarket.sales.application.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record SaleLineResponse(
        UUID id,
        UUID productId,
        String productName,
        int quantity,
        BigDecimal unitPrice,
        BigDecimal subTotal
) {
}
