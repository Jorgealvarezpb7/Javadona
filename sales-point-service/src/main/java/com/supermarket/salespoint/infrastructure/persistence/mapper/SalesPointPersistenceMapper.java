package com.supermarket.salespoint.infrastructure.persistence.mapper;

import com.supermarket.salespoint.domain.model.SalesPoint;
import com.supermarket.salespoint.infrastructure.persistence.entity.SalesPointEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SalesPointPersistenceMapper {

    @Mapping(target = "address", expression = "java(new com.supermarket.salespoint.domain.model.Address(entity.getStreet(), entity.getCity(), entity.getPostalCode(), entity.getProvince()))")
    @Mapping(target = "openingHours", expression = "java(new com.supermarket.salespoint.domain.model.OpeningHours(entity.getOpensAt(), entity.getClosesAt()))")
    SalesPoint toDomain(SalesPointEntity entity);

    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "opensAt", source = "openingHours.opensAt")
    @Mapping(target = "closesAt", source = "openingHours.closesAt")
    SalesPointEntity toEntity(SalesPoint salesPoint);
}
