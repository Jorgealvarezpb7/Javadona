package com.supermarket.salespoint.application.dto;

import com.supermarket.salespoint.domain.model.SalesPointStatus;
import jakarta.validation.constraints.NotNull;

public record SalesPointStatusRequest(
        @NotNull SalesPointStatus status
) {
}
