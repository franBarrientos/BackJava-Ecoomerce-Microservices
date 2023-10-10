package com.ecommerce.Domain.Domain;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Purchase {
    private Long id;

    private PaymentMethod payment;

    private Long customerId;

    private String state;

    private Date createdAt;

    private Date updatedAt;

    private List<PurchaseProduct> purchaseProducts;

}
