package com.supermarket.sales.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Sale {

    private UUID id;
    private UUID customerId;
    private UUID salesPointId;
    private LocalDateTime saleDate;
    private List<SaleLine> lines;
    private BigDecimal totalAmount;
    private PaymentMethod paymentMethod;
    private SaleStatus status;

    public static Sale create(UUID customerId, UUID salesPointId,
                              List<SaleLine> lines, PaymentMethod paymentMethod) {
        Sale sale = new Sale();
        sale.id = UUID.randomUUID();
        sale.customerId = customerId;
        sale.salesPointId = salesPointId;
        sale.saleDate = LocalDateTime.now();
        sale.lines = lines;
        sale.paymentMethod = paymentMethod;
        sale.totalAmount = lines.stream()
                .map(SaleLine::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        sale.status = SaleStatus.COMPLETED;
        return sale;
    }

    /**
     * Reconstruction factory used by the persistence layer to rebuild a Sale
     * from stored data without re-running business logic.
     */
    public static Sale reconstruct(UUID id, UUID customerId, UUID salesPointId,
                                   LocalDateTime saleDate, List<SaleLine> lines,
                                   BigDecimal totalAmount, PaymentMethod paymentMethod,
                                   SaleStatus status) {
        Sale sale = new Sale();
        sale.id = id;
        sale.customerId = customerId;
        sale.salesPointId = salesPointId;
        sale.saleDate = saleDate;
        sale.lines = lines;
        sale.totalAmount = totalAmount;
        sale.paymentMethod = paymentMethod;
        sale.status = status;
        return sale;
    }

    public void refund() {
        this.status = SaleStatus.REFUNDED;
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public UUID getSalesPointId() {
        return salesPointId;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public List<SaleLine> getLines() {
        return lines;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public SaleStatus getStatus() {
        return status;
    }

    public void setId(UUID id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }

    public void setCustomerId(UUID customerId2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCustomerId'");
    }

    public void setSalesPointId(UUID salesPointId2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSalesPointId'");
    }

    public void setSaleDate(LocalDateTime saleDate2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSaleDate'");
    }

    public void setLines(List<SaleLine> lines2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setLines'");
    }

    public void setTotalAmount(BigDecimal totalAmount2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTotalAmount'");
    }

    public void setPaymentMethod(PaymentMethod paymentMethod2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPaymentMethod'");
    }

    public void setStatus(SaleStatus status2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setStatus'");
    }
}
