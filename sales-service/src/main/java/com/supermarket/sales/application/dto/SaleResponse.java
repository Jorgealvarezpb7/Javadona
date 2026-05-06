package com.supermarket.sales.application.dto;

import com.supermarket.sales.domain.model.PaymentMethod;
import com.supermarket.sales.domain.model.SaleStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SaleResponse(
        UUID id,
        UUID customerId,
        UUID salesPointId,
        LocalDateTime saleDate,
        List<SaleLineResponse> lines,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        SaleStatus status
) {
}
