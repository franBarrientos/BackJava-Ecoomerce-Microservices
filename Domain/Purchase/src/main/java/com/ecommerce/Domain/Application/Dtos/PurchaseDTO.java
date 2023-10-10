package com.ecommerce.Domain.Application.Dtos;

import com.ecommerce.Domain.Domain.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDTO {

    private Long id;

    private PaymentMethod payment;

    private CustomerDTO customer;

    private String state;

    private Date createdAt;

    private Date updatedAt;

    @JsonIgnoreProperties({"purchase"})
    private List<PurchaseProductDTO> purchaseProducts;

    private BigDecimal totalSale;
}
