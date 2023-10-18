package com.ecommerce.Domain.Infrastructure.Rest.Responses;

import com.ecommerce.Domain.Application.Dtos.CustomerDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class CustomersPaginatedResponse extends PaginatedResponseBase {
    private List<CustomerDTO> customers;
}
