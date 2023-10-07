package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByHasStockIsTrueAndCategoryId(Pageable pageable, int categoryId);

    Optional<ProductEntity> findByIdAndHasStockIsTrue(Long id);



}
