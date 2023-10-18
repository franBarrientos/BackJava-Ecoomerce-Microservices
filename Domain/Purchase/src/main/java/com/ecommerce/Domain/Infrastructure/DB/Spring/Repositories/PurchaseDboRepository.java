package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Dtos.SalesStatistics;
import com.ecommerce.Domain.Application.Dtos.SalesStatisticsLastdays;
import com.ecommerce.Domain.Application.Dtos.StatisticsCategory;
import com.ecommerce.Domain.Application.Dtos.StatisticsProducts;
import com.ecommerce.Domain.Application.Repositories.PurchaseRepository;
import com.ecommerce.Domain.Domain.Purchase;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.PurchaseEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.PurchaseEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseDboRepository implements PurchaseRepository {

    private final SpringDataPurchaseRepository purchaseRepository;
    private final PurchaseEntityMapper purchaseEntityMapper;


    @Override
    public Page<Purchase> findAll(Pageable pageable) {
        return this.purchaseRepository
                .findAll(pageable)
                .map(purchaseEntityMapper::toDomain);
    }

    @Override
    public List<Purchase> findAllByCustomerId(Long id) {
        return this.purchaseRepository.findAllByCustomerId(id)
                .stream().map(this.purchaseEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public SalesStatistics getStatistics(Pageable productPage, Pageable categoryPage, Pageable daysPage) {
        List<StatisticsProducts> stadisticsProducts = this.purchaseRepository.getStadisticsProducts(productPage);
        List<SalesStatisticsLastdays> stadisticsDays = this.purchaseRepository.getSalesStatisticsLastdays(daysPage);
        return SalesStatistics.builder()
                .salesStatisticsLastdays(stadisticsDays)
                .stadisticsProducts(stadisticsProducts)
                .build();
    }


    @Override
    public Optional<Purchase> findById(Long id) {
        Optional<PurchaseEntity> purchaseEntity = this.purchaseRepository.findById(id);
        return purchaseEntity.isPresent()?
                Optional.of(
                        this.purchaseEntityMapper.toDomain(purchaseEntity.get()))
                :
                Optional.empty();

    }

    @Override
    public Purchase save(Purchase purchase) {

        PurchaseEntity purchaseEntity = this.purchaseEntityMapper.toEntity(purchase);

        purchaseEntity.getPurchaseProducts().forEach(p->p.setPurchase(purchaseEntity));

        return this.purchaseEntityMapper
                .toDomain(this.purchaseRepository
                        .save(purchaseEntity));
    }

    @Override
    public Purchase delete(Purchase purchase) {
        this.purchaseRepository.delete(this.purchaseEntityMapper.toEntity(purchase));
        return purchase;
    }
}
