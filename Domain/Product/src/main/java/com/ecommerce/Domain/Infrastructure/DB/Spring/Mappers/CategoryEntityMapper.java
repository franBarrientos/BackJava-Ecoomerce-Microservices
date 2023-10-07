package com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers;

import com.ecommerce.Domain.Domain.Category;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryEntityMapper {

    Category toDomain(CategoryEntity customerEntity);

    CategoryEntity toEntity(Category customer);
}
