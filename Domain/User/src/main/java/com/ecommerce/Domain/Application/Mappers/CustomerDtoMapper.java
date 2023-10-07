package com.ecommerce.Domain.Application.Mappers;

import com.ecommerce.Domain.Application.Dtos.CustomerDTO;
import com.ecommerce.Domain.Domain.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerDtoMapper {

    CustomerDTO toDto(Customer customer);

    Customer toDomain(CustomerDTO customerDTO);
}
