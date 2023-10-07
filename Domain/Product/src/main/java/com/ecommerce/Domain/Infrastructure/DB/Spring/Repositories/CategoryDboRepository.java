package com.ecommerce.Domain.Infrastructure.DB.Spring.Repositories;

import com.ecommerce.Domain.Application.Repositories.CategoryRepository;
import com.ecommerce.Domain.Domain.Category;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Entities.CategoryEntity;
import com.ecommerce.Domain.Infrastructure.DB.Spring.Mappers.CategoryEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryDboRepository implements CategoryRepository {

    private final SpringDataCategoryRepository categoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Page<Category> findAllByStateIsTrue(Pageable pageable) {
        return this.categoryRepository.findAllByStateIsTrue(pageable)
                .map(categoryEntityMapper::toDomain);
    }

    @Override
    public Optional<Category> findByIdAndStateIsTrue(Long id) {
        Optional<CategoryEntity> category = this.categoryRepository.findByIdAndStateIsTrue(id);
        return category.isPresent()?
               Optional.of(this.categoryEntityMapper.toDomain(category.get()))
                :
                Optional.empty();
    }

    @Override
    public Category save(Category category) {
        return this.categoryEntityMapper.toDomain(
                this.categoryRepository.save(this.categoryEntityMapper.toEntity(category)));
    }


    @Override
    public boolean deleteById(Long id) {
        Optional<CategoryEntity> categoryEntity = this.categoryRepository.findByIdAndStateIsTrue(id);
        if (categoryEntity.isPresent()){
            Category category = this.categoryEntityMapper.toDomain(categoryEntity.get());
            category.setState(false);
            this.categoryRepository.save(this.categoryEntityMapper.toEntity(category));
            return true;
        }else {
            return false;
        }
    }
}
