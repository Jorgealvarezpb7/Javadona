package com.supermarket.sales.infrastructure.persistence.mapper;

import com.supermarket.sales.domain.model.Sale;
import com.supermarket.sales.domain.model.SaleLine;
import com.supermarket.sales.infrastructure.persistence.entity.SaleEntity;
import com.supermarket.sales.infrastructure.persistence.entity.SaleLineEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SalePersistenceMapper {

    public SaleEntity toEntity(Sale sale) {
        SaleEntity entity = new SaleEntity();
        entity.setId(sale.getId());
        entity.setCustomerId(sale.getCustomerId());
        entity.setSalesPointId(sale.getSalesPointId());
        entity.setSaleDate(sale.getSaleDate());
        entity.setTotalAmount(sale.getTotalAmount());
        entity.setPaymentMethod(sale.getPaymentMethod());
        entity.setStatus(sale.getStatus());

        List<SaleLineEntity> lineEntities = sale.getLines().stream()
                .map(line -> toLineEntity(line, entity))
                .toList();
        entity.setLines(lineEntities);

        return entity;
    }

    public SaleLineEntity toLineEntity(SaleLine line, SaleEntity saleEntity) {
        SaleLineEntity lineEntity = new SaleLineEntity();
        lineEntity.setId(line.getId());
        lineEntity.setSale(saleEntity);
        lineEntity.setProductId(line.getProductId());
        lineEntity.setProductName(line.getProductName());
        lineEntity.setQuantity(line.getQuantity());
        lineEntity.setUnitPrice(line.getUnitPrice());
        lineEntity.setSubTotal(line.getSubTotal());
        return lineEntity;
    }

    public Sale toDomain(SaleEntity entity) {
        List<SaleLine> lines = entity.getLines().stream()
                .map(this::toLineDomain)
                .toList();
        return Sale.reconstruct(
                entity.getId(),
                entity.getCustomerId(),
                entity.getSalesPointId(),
                entity.getSaleDate(),
                lines,
                entity.getTotalAmount(),
                entity.getPaymentMethod(),
                entity.getStatus()
        );
    }

    public SaleLine toLineDomain(SaleLineEntity lineEntity) {
        return SaleLine.reconstruct(
                lineEntity.getId(),
                lineEntity.getProductId(),
                lineEntity.getProductName(),
                lineEntity.getQuantity(),
                lineEntity.getUnitPrice(),
                lineEntity.getSubTotal()
        );
    }
}
