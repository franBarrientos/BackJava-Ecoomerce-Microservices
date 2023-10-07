package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataCategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Page<CategoryEntity> findAllByStateIsTrue(Pageable pageable);

    Optional<CategoryEntity> findByIdAndStateIsTrue(Long id);



}
