package com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers;

import com.ecommerce.Domain.Domain.Customer;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.CustomerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserEntityMapper.class)
public interface CustomerEntityMapper {
    @Mapping(target = "user.customer", ignore = true)
    Customer toDomain(CustomerEntity customerEntity);


    CustomerEntity toEntity(Customer customer);
}
