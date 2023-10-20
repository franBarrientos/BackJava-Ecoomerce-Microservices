package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataProductRepository extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByHasStockIsTrueAndCategoryId(Pageable pageable, int categoryId);


    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE %:name% AND p.hasStock = true ")
    Page<ProductEntity> findAllByName(@Param("name") String name, Pageable pageable);

    Optional<ProductEntity> findByIdAndHasStockIsTrue(Long id);

    List<ProductEntity> findAllByFavIsTrueAndHasStockIsTrue();


}
