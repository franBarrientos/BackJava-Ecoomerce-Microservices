package com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers;

import com.ecommerce.Domain.Domain.Purchase;
import com.ecommerce.Domain.Domain.PurchaseProduct;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PurchaseEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PurchaseProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PurchaseEntityMapper {
    Purchase toDomain(PurchaseEntity purchaseEntity);

    @Mapping(target = "purchase", ignore = true)
    PurchaseProduct purchaseProductEntityToPurchaseProduct(PurchaseProductEntity purchaseProductEntity);

    PurchaseEntity toEntity(Purchase purchase);

    @Mapping(target = "purchase", ignore = true)
    PurchaseProductEntity purchaseProductToPurchaseProductEntity(PurchaseProduct purchaseProduct);

}
