package com.supermarket.sales.domain.service;

import com.supermarket.sales.application.dto.CreateSaleRequest;
import com.supermarket.sales.domain.model.Sale;
import com.supermarket.sales.domain.model.SaleLine;
import com.supermarket.sales.domain.repository.SaleRepository;
import com.supermarket.sales.infrastructure.client.CustomerServiceClient;
import com.supermarket.sales.infrastructure.client.InventoryServiceClient;
import com.supermarket.sales.infrastructure.client.SalesPointServiceClient;
import com.supermarket.sales.infrastructure.client.dto.CustomerClientDto;
import com.supermarket.sales.infrastructure.client.dto.SalesPointClientDto;
import com.supermarket.sales.infrastructure.exception.CustomerNotActiveException;
import com.supermarket.sales.infrastructure.exception.SaleNotFoundException;
import com.supermarket.sales.infrastructure.exception.SalesPointNotOpenException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SaleDomainService {

    private final SaleRepository saleRepository;
    private final CustomerServiceClient customerServiceClient;
    private final SalesPointServiceClient salesPointServiceClient;
    private final InventoryServiceClient inventoryServiceClient;

    public SaleDomainService(SaleRepository saleRepository,
                              CustomerServiceClient customerServiceClient,
                              SalesPointServiceClient salesPointServiceClient,
                              InventoryServiceClient inventoryServiceClient) {
        this.saleRepository = saleRepository;
        this.customerServiceClient = customerServiceClient;
        this.salesPointServiceClient = salesPointServiceClient;
        this.inventoryServiceClient = inventoryServiceClient;
    }

    public Sale createSale(CreateSaleRequest request) {
        CustomerClientDto customer = customerServiceClient.getCustomer(request.customerId());
        if (!"ACTIVE".equalsIgnoreCase(customer.status())) {
            throw new CustomerNotActiveException(request.customerId());
        }

        SalesPointClientDto salesPoint = salesPointServiceClient.getSalesPoint(request.salesPointId());
        if (!"OPEN".equalsIgnoreCase(salesPoint.status())) {
            throw new SalesPointNotOpenException(request.salesPointId());
        }

        inventoryServiceClient.decrementStock(request.salesPointId(), request.lines());

        List<SaleLine> lines = request.lines().stream()
                .map(l -> SaleLine.create(l.productId(), l.productName(), l.quantity(), l.unitPrice()))
                .toList();

        Sale sale = Sale.create(request.customerId(), request.salesPointId(), lines, request.paymentMethod());
        Sale saved = saleRepository.save(sale);

        customerServiceClient.addRewardPoints(request.customerId(), saved.getTotalAmount().intValue());
        customerServiceClient.linkSalesPoint(request.customerId(), request.salesPointId());

        return saved;
    }

    public Sale refundSale(UUID id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));

        List<CreateSaleRequest.SaleLineRequest> lineRequests = sale.getLines().stream()
                .map(l -> new CreateSaleRequest.SaleLineRequest(
                        l.getProductId(), l.getProductName(), l.getQuantity(), l.getUnitPrice()))
                .toList();

        inventoryServiceClient.incrementStock(sale.getSalesPointId(), lineRequests);
        sale.refund();
        return saleRepository.save(sale);
    }

    @Transactional(readOnly = true)
    public Sale findById(UUID id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Sale> findAll(Pageable pageable) {
        return saleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Sale> findByCustomerId(UUID customerId, Pageable pageable) {
        return saleRepository.findByCustomerId(customerId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Sale> findBySalesPointId(UUID salesPointId, Pageable pageable) {
        return saleRepository.findBySalesPointId(salesPointId, pageable);
    }
}
