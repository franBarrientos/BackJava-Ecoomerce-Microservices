package com.ecommerce.Domain.Application.Dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductAddDTO {
    @NotEmpty
    private  String name;

    @NotEmpty
    private  String description;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long category;

    @NotNull
    private MultipartFile img;

    @NotNull
    private Long stock;

    private Boolean fav;
}
