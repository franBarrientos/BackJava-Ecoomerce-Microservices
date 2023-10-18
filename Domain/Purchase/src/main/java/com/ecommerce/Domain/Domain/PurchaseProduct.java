package com.ecommerce.Domain.Domain;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseProduct {
    private Long id;

    private Purchase purchase;

    private Long productId;

    private Integer quantity;

    private BigDecimal totalPrice;

}
