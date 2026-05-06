package com.supermarket.sales.infrastructure.client;

import com.supermarket.sales.infrastructure.client.dto.SalesPointClientDto;
import com.supermarket.sales.infrastructure.exception.SalesPointNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

@Component
public class SalesPointServiceClient {

    private final RestClient restClient;

    public SalesPointServiceClient(@Value("${services.sales-point.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public SalesPointClientDto getSalesPoint(UUID salesPointId) {
        try {
            return restClient.get()
                    .uri("/api/v1/sales-points/{id}", salesPointId)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (req, res) -> {
                        throw new SalesPointNotFoundException(salesPointId);
                    })
                    .body(SalesPointClientDto.class);
        } catch (RestClientException e) {
            throw new SalesPointNotFoundException(salesPointId);
        }
    }
}
