package com.ecommerce.Domain.Application.Dtos;

import com.ecommerce.Domain.Domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseAddDTO {

    @NotNull
    private PaymentMethod payment;

    @NotNull
    private Long customerId;

    @NotNull
    private List<PurchaseProductAddDTO> purchaseProducts;
}
