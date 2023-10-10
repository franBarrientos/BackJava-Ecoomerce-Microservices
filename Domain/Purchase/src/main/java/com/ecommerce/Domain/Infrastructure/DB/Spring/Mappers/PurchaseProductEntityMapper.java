package com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers;

import com.ecommerce.Domain.Domain.PurchaseProduct;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PurchaseProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public interface PurchaseProductEntityMapper {
    @Mapping(target = "purchase.purchaseProducts", ignore = true)
    PurchaseProduct toDomain(PurchaseProductEntity entity);


    @Mapping(target = "purchase.purchaseProducts", ignore = true)
    PurchaseProductEntity toEntity(PurchaseProduct domain);
}
