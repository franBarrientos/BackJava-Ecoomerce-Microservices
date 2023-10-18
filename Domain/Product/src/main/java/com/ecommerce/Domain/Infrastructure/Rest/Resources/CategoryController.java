package com.ecommerce.Domain.Infrastructure.Rest.Resources;


import com.ecommerce.Domain.Application.Dtos.CategoryAddDTO;
import com.ecommerce.Domain.Application.Dtos.CategoryDTO;
import com.ecommerce.Domain.Application.Dtos.ProductAddDTO;
import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.ecommerce.Domain.Application.Services.CategoryService;
import com.ecommerce.Domain.Infrastructure.Rest.Config.ApiResponse;
import com.ecommerce.Domain.Infrastructure.Rest.Responses.CategoriesPaginatedResponse;
import com.ecommerce.Domain.Infrastructure.Rest.Responses.ProductsPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int limit) {
        Page<CategoryDTO> categories = this.categoryService.getAll(PageRequest.of(page, limit));

        return ApiResponse.oK(
                CategoriesPaginatedResponse.builder()
                        .categories(categories.getContent())
                        .totalPages(categories.getTotalPages())
                        .totalItems(categories.getNumberOfElements())
                        .build()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable(value = "id") Long id) {
        return ApiResponse.oK(this.categoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOne(@ModelAttribute CategoryAddDTO body) {
        return ApiResponse.create(this.categoryService.createOne(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOne(@PathVariable(value = "id") Long id,
                                                 @ModelAttribute CategoryAddDTO body){
        return ApiResponse.oK(this.categoryService.updateById(id, body));
    }


   @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOne(@PathVariable(value = "id") Long id){
        return ApiResponse.oK( this.categoryService.deleteById(id));
    }


}
