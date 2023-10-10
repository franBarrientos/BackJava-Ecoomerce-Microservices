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
public class SalesStadistics {

    private List<StadisticsProducts> stadisticsProducts;
    private List<StadisticsCategory> stadisticsCategories;
    private List<StadisticsLast10days> stadisticsLast10days;
}
