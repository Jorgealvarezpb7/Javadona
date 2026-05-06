package com.supermarket.inventory.application.mapper;

import com.supermarket.inventory.application.dto.ProductResponse;
import com.supermarket.inventory.application.dto.StockResponse;
import com.supermarket.inventory.domain.model.Product;
import com.supermarket.inventory.domain.model.StoreInventory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    @Mapping(target = "category", expression = "java(product.getCategory().name())")
    @Mapping(target = "barcodeValue", expression = "java(product.getBarcode().value())")
    ProductResponse toProductResponse(Product product);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "salesPointId", source = "salesPointId")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "currentStock", source = "currentStock")
    @Mapping(target = "minimumStock", source = "minimumStock")
    @Mapping(target = "lastUpdated", source = "lastUpdated")
    StockResponse toStockResponse(StoreInventory inventory);
}
