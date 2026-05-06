package com.supermarket.sales.application.dto;

import com.supermarket.sales.domain.model.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreateSaleRequest(
        @NotNull UUID customerId,
        @NotNull UUID salesPointId,
        @NotNull PaymentMethod paymentMethod,
        @NotEmpty @Valid List<SaleLineRequest> lines
) {
    public record SaleLineRequest(
            @NotNull UUID productId,
            @NotBlank String productName,
            @Min(1) int quantity,
            @NotNull @Positive BigDecimal unitPrice
    ) {
    }
}
