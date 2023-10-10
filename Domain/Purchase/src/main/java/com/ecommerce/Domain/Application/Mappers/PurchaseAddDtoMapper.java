package com.ecommerce.Domain.Application.Mappers;

import com.ecommerce.Domain.Application.Client.ProductClient;
import com.ecommerce.Domain.Application.Client.UserClient;
import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.ecommerce.Domain.Application.Dtos.PurchaseAddDTO;
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
                        .map(p -> {
                            ProductDTO productDto = this.productClient.getProductDTO(p.getProductId());

                            if(productDto == null) throw new NotFoundException("Product " + p.getProductId() + " not found");

                            return PurchaseProduct.builder()
                                    .productId(p.getProductId())
                                    .quantity(p.getQuantity())
                                    .totalPrice(productDto.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                                    .build();
                        }).collect(Collectors.toList()))
                .build();
    }

  /*  public Purchase purchaseAddDTOtoDomainWithPurchaseOptional(PurchaseAddDTO purchaseAddDTO) {
        return Purchase.builder()
                .payment(purchaseAddDTO.getPayment() != null ? purchaseAddDTO.getPayment()
                        : null)
                .customer(
                        purchaseAddDTO.getCustomerId() != null ?
                                (this.customerRepository
                                        .findById(purchaseAddDTO.getCustomerId())
                                        .orElseThrow(() -> new NotFoundException("customer "
                                                + purchaseAddDTO.getCustomerId() +
                                                " not found")))
                                : null)
                .purchaseProducts(
                        purchaseAddDTO.getPurchaseProducts() != null ?
                                purchaseAddDTO
                                        .getPurchaseProducts()
                                        .stream()
                                        .map(p -> {
                                            Product product = this.productRepository
                                                    .findByIdAndHasStockIsTrue(p.getProductId())
                                                    .orElseThrow(() -> new NotFoundException("Product " +
                                                            p.getProductId() + " not found"));

                                            return PurchaseProduct.builder()
                                                    .product(product)
                                                    .quantity(p.getQuantity())
                                                    .totalPrice(product.getPrice()
                                                            .multiply(BigDecimal.valueOf(p.getQuantity())))
                                                    .build();
                                        }).collect(Collectors.toList())
                                : null)
                .build();
    }*/

}
