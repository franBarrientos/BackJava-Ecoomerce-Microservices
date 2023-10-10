package com.ecommerce.Domain.Application.Mappers;

import com.ecommerce.Domain.Application.Dtos.PurchaseDTO;
import com.ecommerce.Domain.Domain.Purchase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface PurchaseDtoMapper {
    @Mapping(target = "totalSale", expression = "java(calculateTotalSale(purchase))")
    PurchaseDTO toDto(Purchase purchase);

    default BigDecimal calculateTotalSale(Purchase purchase) {
        return purchase.getPurchaseProducts()
                .stream()
                .map(pp -> pp.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    };

    Purchase toDomain(PurchaseDTO purchaseDTO);

}
