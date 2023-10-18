package com.ecommerce.Domain.Application.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class SalesStatistics {

    private List<StatisticsProducts> stadisticsProducts;
    private List<StatisticsCategory> stadisticsCategories;
    private List<SalesStatisticsLastdays> salesStatisticsLastdays;
}
