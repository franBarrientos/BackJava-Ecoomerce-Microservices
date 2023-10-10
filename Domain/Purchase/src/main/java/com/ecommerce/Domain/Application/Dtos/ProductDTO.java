package com.ecommerce.Domain.Application.Dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder @Data
public class ProductDTO {

    private Long id;

    @NotEmpty
    private  String name;

    @NotEmpty
    private  String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private CategoryDTO category;

    private  String img;

    @NotNull
    private Long stock;

    private Boolean hasStock;

    private Boolean fav;
}
