package com.ecommerce.Domain.Infrastructure.Rest.Responses;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
public abstract class PaginatedResponseBase {
    protected int totalItems;
    protected int totalPages;
}
