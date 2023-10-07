package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductRepository {
    Page<Product> findAllByHasStockIsTrue(Pageable pageable, int categoryId);

    Optional<Product> findByIdAndHasStockIsTrue(Long id);

    Product save(Product product);
}
