package com.ecommerce.Domain.Application.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StadisticsCategory {
    private Long id;
    private String category;
    private Long totalSale;
}
