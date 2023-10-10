package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Application.Dtos.StadisticsLast10days;
import com.ecommerce.Domain.Domain.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PurchaseRepository {
    Page<Purchase> findAll(Pageable pageable);

/*
    Page<Purchase> search(Integer dni, String firstName, String lastName, Pageable pageable);
*/

    List<StadisticsLast10days> getLast10DaysStadistics();

    Optional<Purchase> findById(Long id);

    Purchase save(Purchase purchase);

    Purchase delete(Purchase purchase);
}
