package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryRepository {
    Page<Category> findAllByStateIsTrue(Pageable pageable);
    Optional<Category> findByIdAndStateIsTrue(Long id);
    Category save(Category category);

    boolean deleteById(Long id);
}
