package com.supermarket.customer.application.mapper;

import com.supermarket.customer.application.dto.CustomerResponse;
import com.supermarket.customer.domain.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "documentType", source = "documentId.type")
    @Mapping(target = "documentValue", source = "documentId.value")
    @Mapping(target = "firstName", source = "personalDetails.firstName")
    @Mapping(target = "lastName", source = "personalDetails.lastName")
    @Mapping(target = "dateOfBirth", source = "personalDetails.dateOfBirth")
    @Mapping(target = "phoneNumber", source = "personalDetails.phoneNumber")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "street", source = "address.street")
    @Mapping(target = "city", source = "address.city")
    @Mapping(target = "postalCode", source = "address.postalCode")
    @Mapping(target = "province", source = "address.province")
    @Mapping(target = "status", source = "status")
    CustomerResponse toResponse(Customer customer);
}
