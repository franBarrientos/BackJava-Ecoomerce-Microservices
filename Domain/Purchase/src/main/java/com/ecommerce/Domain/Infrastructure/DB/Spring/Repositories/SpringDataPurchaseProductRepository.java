package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PurchaseProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPurchaseProductRepository extends JpaRepository<PurchaseProductEntity, Long> {
}
