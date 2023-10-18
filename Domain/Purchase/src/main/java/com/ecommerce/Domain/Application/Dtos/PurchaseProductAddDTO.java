package com.ecommerce.Domain.Application.Dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseProductAddDTO {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

}
