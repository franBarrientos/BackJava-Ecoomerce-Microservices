package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;


import com.ecommerce.Domain.Application.Repositories.ProductRepository;
import com.ecommerce.Domain.Domain.Product;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.ProductEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductDboRepository implements ProductRepository {

    private final SpringDataProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public Page<Product> findAllByHasStockIsTrue(Pageable pageable, int categoryId) {
        return this.productRepository.findByHasStockIsTrueAndCategoryId(pageable, categoryId)
                .map(productEntityMapper::toDomain);
    }

    @Override
    public Optional<Product> findByIdAndHasStockIsTrue(Long id) {
        Optional<ProductEntity> productEntity = this.productRepository.findByIdAndHasStockIsTrue(id);
        return productEntity.isPresent()?
                Optional.of(
                        this.productEntityMapper.toDomain(productEntity.get()))
                :
                Optional.empty();

    }


    @Override
    public Product save(Product product) {
        return this.productEntityMapper.toDomain(
                this.productRepository.save(this.productEntityMapper.toEntity(product))
        );
    }
}
