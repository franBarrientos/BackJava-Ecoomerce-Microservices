package com.ecommerce.Domain.Application.Mappers;

import com.ecommerce.Domain.Application.Dtos.CategoryDTO;
import com.ecommerce.Domain.Domain.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryDtoMapper {
    CategoryDTO toDto(Category category);

    Category toDomain(CategoryDTO categoryDTO);
}
