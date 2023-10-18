package com.ecommerce.Domain.Infrastructure.DB.Spring.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "PurchaseProduct")
@Table(name = "`purchases_products`")
public class PurchaseProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "purchaseId")
    private PurchaseEntity purchase;

    private Long productId;

    private Integer quantity;

    private BigDecimal totalPrice;
}
