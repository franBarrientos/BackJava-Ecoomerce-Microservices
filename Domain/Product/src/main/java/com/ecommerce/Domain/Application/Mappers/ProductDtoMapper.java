package com.ecommerce.Domain.Application.Mappers;

import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.ecommerce.Domain.Domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductDtoMapper{

    ProductDTO toDto(Product user);

    Product toDomain(ProductDTO userDto);
}
