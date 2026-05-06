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

        Sale sale = new Sale();
        sale.setId(entity.getId());
        sale.setCustomerId(entity.getCustomerId());
        sale.setSalesPointId(entity.getSalesPointId());
        sale.setSaleDate(entity.getSaleDate());
        sale.setLines(lines);
        sale.setTotalAmount(entity.getTotalAmount());
        sale.setPaymentMethod(entity.getPaymentMethod());
        sale.setStatus(entity.getStatus());
        return sale;
    }

    public SaleLine toLineDomain(SaleLineEntity lineEntity) {
        SaleLine line = new SaleLine();
        line.setId(lineEntity.getId());
        line.setProductId(lineEntity.getProductId());
        line.setProductName(lineEntity.getProductName());
        line.setQuantity(lineEntity.getQuantity());
        line.setUnitPrice(lineEntity.getUnitPrice());
        line.setSubTotal(lineEntity.getSubTotal());
        return line;
    }
}
