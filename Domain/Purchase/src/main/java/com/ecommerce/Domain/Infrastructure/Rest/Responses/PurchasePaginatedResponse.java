package com.ecommerce.Domain.Infrastructure.Rest.Responses;

import com.ecommerce.Domain.Application.Dtos.PurchaseDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class PurchasePaginatedResponse extends PaginatedResponseBase {
    private List<PurchaseDTO> purchases;
}
