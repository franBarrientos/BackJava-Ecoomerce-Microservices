package com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers;

import com.ecommerce.Domain.Domain.Product;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {

    Product toDomain(ProductEntity entity);


    ProductEntity toEntity(Product domain);
}

