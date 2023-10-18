package com.ecommerce.Domain.Application.Services;

import com.ecommerce.Domain.Application.Dtos.CategoryAddDTO;
import com.ecommerce.Domain.Application.Dtos.CategoryDTO;
import com.ecommerce.Domain.Application.Dtos.ProductAddDTO;
import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.ecommerce.Domain.Application.Exceptions.NotFoundException;
import com.ecommerce.Domain.Application.Mappers.CategoryDtoMapper;
import com.ecommerce.Domain.Application.Repositories.CategoryRepository;
import com.ecommerce.Domain.Application.Repositories.UploadFileRepository;
import com.ecommerce.Domain.Domain.Category;
import com.ecommerce.Domain.Domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDtoMapper categoryDtoMapper;
    private final UploadFileRepository uploadFileRepository;




    public Page<CategoryDTO> getAll(Pageable pageable) {
        Page<Category> categories = this.categoryRepository.findAllByStateIsTrue(pageable);


        List<CategoryDTO> categoryDTOS = categories.get()
                .parallel()
                .map(this.categoryDtoMapper::toDto)
                .collect(Collectors.toList());

        return new PageImpl<>(categoryDTOS, pageable, categories.getTotalElements());
    }

    public CategoryDTO getById(Long id) {
        return this.categoryDtoMapper.toDto
                (this.categoryRepository.findByIdAndStateIsTrue(id)
                        .orElseThrow(() -> new NotFoundException(" Category " + id + " not found")));
    }

    public CategoryDTO createOne(CategoryAddDTO categoryAddDTO) {

        String imgUrl = this.uploadFileRepository.uploadImage(categoryAddDTO.getImg());

        Category category = this.categoryRepository.save
                (Category.builder()
                        .name(categoryAddDTO.getName())
                        .img(imgUrl)
                        .build());

        return this.categoryDtoMapper.toDto(category);
    }

    public CategoryDTO updateById(Long id, CategoryAddDTO categoryAddDTO){

        Category category = this.categoryRepository.findByIdAndStateIsTrue(id)
                .orElseThrow(() -> new NotFoundException(" Category " + id + " not found"));

        if(categoryAddDTO.getName() != null && !categoryAddDTO.getName().isBlank()){
            category.setName(categoryAddDTO.getName());
        }

        if(categoryAddDTO.getImg() != null){
            this.uploadFileRepository.overrideImage(categoryAddDTO.getImg(),category.getImg());
        }


        return this.categoryDtoMapper.toDto
                (this.categoryRepository.save(category));
    }


    public CategoryDTO deleteById(long id) {
        Category category = this.categoryRepository.findByIdAndStateIsTrue(id)
                .orElseThrow(() -> new NotFoundException(" Category " + id + " not found"));


        category.setState(false);

        return this.categoryDtoMapper.toDto
                (this.categoryRepository.save(category));
    }
}
