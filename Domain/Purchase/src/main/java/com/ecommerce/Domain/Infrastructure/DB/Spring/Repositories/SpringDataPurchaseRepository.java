package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Dtos.StadisticsLast10days;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataPurchaseRepository extends JpaRepository<PurchaseEntity, Long> {

 /*   @Query("SELECT p FROM Purchase p " +
            "WHERE p.customer.dni = :dni " +
            "OR p.customer.user.firstName LIKE %:firstName% " +
            "OR p.customer.user.lastName LIKE %:lastName%")
    Page<PurchaseEntity> search(Integer dni, String firstName, String lastName, Pageable pageable);
*/
    @Query(value = "SELECT new com.ecommerce.Domain.Application.Dtos.StadisticsLast10days( " +
            "p.createdAt, SUM(pp.totalPrice)) FROM Purchase p " +
            "JOIN p.purchaseProducts pp GROUP BY p.createdAt ORDER BY p.createdAt " +
            "LIMIT 10 ")
    List<StadisticsLast10days> getLast10DaysStadistics();
}
