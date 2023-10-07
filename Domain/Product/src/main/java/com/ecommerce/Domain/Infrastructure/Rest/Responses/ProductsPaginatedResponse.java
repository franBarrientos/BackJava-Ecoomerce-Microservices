package com.ecommerce.Domain.Infrastructure.Rest.Responses;

import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class ProductsPaginatedResponse extends PaginatedResponseBase {
    private List<ProductDTO> products;
}
