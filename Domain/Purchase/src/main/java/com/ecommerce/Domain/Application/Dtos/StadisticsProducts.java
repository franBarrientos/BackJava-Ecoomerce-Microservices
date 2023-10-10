package com.ecommerce.Domain.Application.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StadisticsProducts {
    private Long id;
    private String product;
    private Long totalSale;

}
