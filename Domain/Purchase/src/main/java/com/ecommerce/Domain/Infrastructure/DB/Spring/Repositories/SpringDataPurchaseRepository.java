package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Dtos.SalesStatisticsLastdays;
import com.ecommerce.Domain.Application.Dtos.StatisticsCategory;
import com.ecommerce.Domain.Application.Dtos.StatisticsProducts;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PurchaseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataPurchaseRepository extends JpaRepository<PurchaseEntity, Long> {


    @Query(value = "SELECT new com.ecommerce.Domain.Application.Dtos.StatisticsProducts( " +
            "pp.productId, SUM(pp.totalPrice)) FROM PurchaseProduct pp " +
            "GROUP BY pp.productId " +
            "ORDER BY SUM(pp.totalPrice) DESC ")
    List<StatisticsProducts> getStadisticsProducts(Pageable pageable);

    @Query("SELECT p FROM Purchase p WHERE p.customerId = :id")
    List<PurchaseEntity> findAllByCustomerId(@Param("id") Long id);

    @Query(value = "SELECT new com.ecommerce.Domain.Application.Dtos.SalesStatisticsLastdays( " +
            "DATE(p.createdAt), SUM(pp.totalPrice)) FROM PurchaseProduct pp " +
            "JOIN pp.purchase p " +
            "GROUP BY DATE(p.createdAt) " +
            "ORDER BY SUM(pp.totalPrice) DESC ")
    List<SalesStatisticsLastdays> getSalesStatisticsLastdays(Pageable daysPage);
}
