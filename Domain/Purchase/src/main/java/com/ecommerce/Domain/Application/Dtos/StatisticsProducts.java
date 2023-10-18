package com.ecommerce.Domain.Application.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsProducts {

    private ProductDTO product;
    private BigDecimal totalSale;

    public StatisticsProducts ( Long productId, BigDecimal totalSale) {
        this.product = new ProductDTO()
                .builder()
                .id(productId)
                .build();
        this.totalSale = totalSale;
    }
}
