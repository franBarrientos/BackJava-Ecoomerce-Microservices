package com.ecommerce.Domain.Infrastructure.Rest.Responses;

import com.ecommerce.Domain.Application.Dtos.CategoryDTO;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
public class CategoriesPaginatedResponse extends PaginatedResponseBase{
    private List<CategoryDTO> categories;
}
