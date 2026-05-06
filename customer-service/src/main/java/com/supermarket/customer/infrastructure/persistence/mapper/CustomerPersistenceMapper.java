package com.supermarket.customer.infrastructure.persistence.mapper;

import com.supermarket.customer.domain.model.Address;
import com.supermarket.customer.domain.model.Customer;
import com.supermarket.customer.domain.model.DocumentId;
import com.supermarket.customer.domain.model.Email;
import com.supermarket.customer.domain.model.PersonalDetails;
import com.supermarket.customer.infrastructure.persistence.entity.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerPersistenceMapper {

    @Mapping(target = "documentId", expression = "java(new com.supermarket.customer.domain.model.DocumentId(entity.getDocumentType(), entity.getDocumentValue()))")
    @Mapping(target = "personalDetails", expression = "java(new com.supermarket.customer.domain.model.PersonalDetails(entity.getFirstName(), entity.getLastName(), entity.getDateOfBirth(), entity.getPhoneNumber()))")
    @Mapping(target = "email", expression = "java(new com.supermarket.customer.domain.model.Email(entity.getEmail()))")
    @Mapping(target = "address", expression = "java(new com.supermarket.customer.domain.model.Address(entity.getStreet(), entity.getCity(), entity.getPostalCode(), entity.getProvince()))")
    Customer toDomain(CustomerEntity entity);

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
    CustomerEntity toEntity(Customer customer);
}
