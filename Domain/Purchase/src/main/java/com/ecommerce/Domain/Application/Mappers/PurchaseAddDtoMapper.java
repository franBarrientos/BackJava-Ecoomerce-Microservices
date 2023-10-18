package com.ecommerce.Domain.Application.Mappers;

import com.ecommerce.Domain.Application.Client.ProductClient;
import com.ecommerce.Domain.Application.Client.UserClient;
import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.ecommerce.Domain.Application.Dtos.PurchaseAddDTO;
import com.ecommerce.Domain.Application.Dtos.PurchaseProductAddDTO;
import com.ecommerce.Domain.Application.Exceptions.NotFoundException;
import com.ecommerce.Domain.Domain.Purchase;
import com.ecommerce.Domain.Domain.PurchaseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PurchaseAddDtoMapper {
    private final ProductClient productClient;

    public Purchase purchaseAddDTOtoDomain(PurchaseAddDTO purchaseAddDTO) {

        return Purchase.builder()
                .payment(purchaseAddDTO.getPayment())
                .customerId(purchaseAddDTO.getCustomerId())
                .purchaseProducts(purchaseAddDTO
                        .getPurchaseProducts()
                        .stream()
                        .map(this::purchaseProductAddDTOtoDomain)
                        .collect(Collectors.toList()))
                .build();
    }

    public Purchase purchaseAddDTOtoDomainWithPurchaseOptional(PurchaseAddDTO purchaseAddDTO) {
        return Purchase.builder()
                .payment(purchaseAddDTO.getPayment() != null ? purchaseAddDTO.getPayment()
                        : null)

                .purchaseProducts(
                        purchaseAddDTO.getPurchaseProducts() != null ?
                                purchaseAddDTO
                                        .getPurchaseProducts()
                                        .stream()
                                        .map(this::purchaseProductAddDTOtoDomain)
                                        .collect(Collectors.toList())
                                : null)
                .build();
    }

    public PurchaseProduct purchaseProductAddDTOtoDomain(PurchaseProductAddDTO purchaseProductAddDTO) {

        ProductDTO productDto = this.productClient.getProductDTO(purchaseProductAddDTO.getProductId());

        if(productDto == null) throw new NotFoundException("Product " + purchaseProductAddDTO.getProductId() + " not found");

        return PurchaseProduct.builder()
                .productId(purchaseProductAddDTO.getProductId())
                .quantity(purchaseProductAddDTO.getQuantity())
                .totalPrice(productDto.getPrice().multiply(BigDecimal.valueOf(purchaseProductAddDTO.getQuantity())))
                .build();
    }


}
