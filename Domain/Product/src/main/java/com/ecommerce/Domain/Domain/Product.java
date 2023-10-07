package com.ecommerce.Domain.Domain;

import lombok.*;
import com.ecommerce.Domain.Domain.Category;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    private Category category;

    private String img;

    private Long stock;

    private Boolean hasStock;

    private Boolean fav;

    private Date createdAt;

    private Date updatedAt;

}
