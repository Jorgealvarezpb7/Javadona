package com.supermarket.salespoint.application.mapper;

import com.supermarket.salespoint.application.dto.SalesPointResponse;
import com.supermarket.salespoint.domain.model.SalesPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalesPointMapper {

    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "opensAt", expression = "java(salesPoint.getOpeningHours().opensAt().toString())")
    @Mapping(target = "closesAt", expression = "java(salesPoint.getOpeningHours().closesAt().toString())")
    @Mapping(target = "status", expression = "java(salesPoint.getStatus().name())")
    SalesPointResponse toResponse(SalesPoint salesPoint);
}
