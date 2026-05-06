package com.supermarket.sales.domain.model;

import java.math.BigDecimal;
import java.util.UUID;

public class SaleLine {

    private UUID id;
    private UUID productId;
    private String productName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;

    public static SaleLine create(UUID productId, String productName, int quantity, BigDecimal unitPrice) {
        SaleLine line = new SaleLine();
        line.id = UUID.randomUUID();
        line.productId = productId;
        line.productName = productName;
        line.quantity = quantity;
        line.unitPrice = unitPrice;
        line.subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        return line;
    }

    /**
     * Reconstruction factory used by the persistence layer to rebuild a SaleLine
     * from stored data without recalculating derived fields.
     */
    public static SaleLine reconstruct(UUID id, UUID productId, String productName,
                                       int quantity, BigDecimal unitPrice, BigDecimal subTotal) {
        SaleLine line = new SaleLine();
        line.id = id;
        line.productId = productId;
        line.productName = productName;
        line.quantity = quantity;
        line.unitPrice = unitPrice;
        line.subTotal = subTotal;
        return line;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setId(UUID id2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setId'");
    }

    public void setProductId(UUID productId2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProductId'");
    }

    public void setProductName(String productName2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setProductName'");
    }

    public void setQuantity(int quantity2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setQuantity'");
    }

    public void setUnitPrice(BigDecimal unitPrice2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setUnitPrice'");
    }

    public void setSubTotal(BigDecimal subTotal2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setSubTotal'");
    }
}
