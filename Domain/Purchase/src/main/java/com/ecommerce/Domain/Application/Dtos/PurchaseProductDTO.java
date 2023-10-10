package com.ecommerce.Domain.Application.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProductDTO {
    private Long id;

    private PurchaseDTO purchase;

    private ProductDTO product;

    private Integer quantity;

    private BigDecimal totalPrice;
}
