package com.ecommerce.Domain.Application.Repositories;

import com.ecommerce.Domain.Application.Dtos.SalesStatistics;
import com.ecommerce.Domain.Application.Dtos.SalesStatisticsLastdays;
import com.ecommerce.Domain.Domain.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.BaseStream;

public interface PurchaseRepository {
    Page<Purchase> findAll(Pageable pageable);

    List<Purchase> findAllByCustomerId(Long id);

    SalesStatistics getStatistics(Pageable productPage, Pageable categoryPage, Pageable daysPage);

    Optional<Purchase> findById(Long id);

    Purchase save(Purchase purchase);

    Purchase delete(Purchase purchase);

}
