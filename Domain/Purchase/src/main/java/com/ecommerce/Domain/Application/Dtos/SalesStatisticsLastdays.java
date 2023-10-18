package com.ecommerce.Domain.Application.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
@AllArgsConstructor
public class SalesStatisticsLastdays {
    private Date date;
    private BigDecimal totalSales;
}
