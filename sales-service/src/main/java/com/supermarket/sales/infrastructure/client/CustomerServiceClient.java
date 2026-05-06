package com.supermarket.sales.infrastructure.client;

import com.supermarket.sales.infrastructure.client.dto.CustomerClientDto;
import com.supermarket.sales.infrastructure.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;
import java.util.UUID;

@Component
public class CustomerServiceClient {

    private final RestClient restClient;

    public CustomerServiceClient(@Value("${services.customer.url}") String baseUrl) {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public CustomerClientDto getCustomer(UUID customerId) {
        try {
            return restClient.get()
                    .uri("/api/v1/customers/{id}", customerId)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (req, res) -> {
                        throw new CustomerNotFoundException(customerId);
                    })
                    .body(CustomerClientDto.class);
        } catch (RestClientException e) {
            throw new CustomerNotFoundException(customerId);
        }
    }

    public void addRewardPoints(UUID customerId, int points) {
        restClient.patch()
                .uri("/api/v1/customers/{id}/rewards", customerId)
                .body(Map.of("points", points))
                .retrieve()
                .toBodilessEntity();
    }

    public void linkSalesPoint(UUID customerId, UUID salesPointId) {
        restClient.post()
                .uri("/api/v1/customers/{id}/sales-points/{spId}", customerId, salesPointId)
                .retrieve()
                .toBodilessEntity();
    }
}
