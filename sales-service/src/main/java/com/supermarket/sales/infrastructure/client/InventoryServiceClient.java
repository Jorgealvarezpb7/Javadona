package com.supermarket.sales.infrastructure.client;

import com.supermarket.sales.application.dto.CreateSaleRequest;
import com.supermarket.sales.infrastructure.client.dto.StockDecrementRequest;
import com.supermarket.sales.infrastructure.client.dto.StockIncrementRequest;
import com.supermarket.sales.infrastructure.exception.InsufficientStockException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.UUID;

@Component
public class InventoryServiceClient {

    private final RestClient restClient;

    public InventoryServiceClient(@Value("${services.inventory.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public void decrementStock(UUID salesPointId, List<CreateSaleRequest.SaleLineRequest> lines) {
        List<StockDecrementRequest.StockLineItem> items = lines.stream()
                .map(l -> new StockDecrementRequest.StockLineItem(l.productId(), l.quantity()))
                .toList();
        restClient.post()
                .uri("/api/v1/inventory/stock/decrement")
                .body(new StockDecrementRequest(salesPointId, items))
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new InsufficientStockException();
                })
                .toBodilessEntity();
    }

    public void incrementStock(UUID salesPointId, List<CreateSaleRequest.SaleLineRequest> lines) {
        List<StockIncrementRequest.StockLineItem> items = lines.stream()
                .map(l -> new StockIncrementRequest.StockLineItem(l.productId(), l.quantity()))
                .toList();
        restClient.post()
                .uri("/api/v1/inventory/stock/increment")
                .body(new StockIncrementRequest(salesPointId, items))
                .retrieve()
                .toBodilessEntity();
    }
}
