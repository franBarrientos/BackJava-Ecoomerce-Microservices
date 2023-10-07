package com.ecommerce.Domain.Infrastructure.Rest.Resources;


import com.ecommerce.Domain.Application.Dtos.ProductAddDTO;
import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.ecommerce.Domain.Application.Services.ProductService;
import com.ecommerce.Domain.Infrastructure.Rest.Config.ApiResponse;
import com.ecommerce.Domain.Infrastructure.Rest.Responses.ProductsPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int limit,
            @RequestParam(defaultValue = "1") int category) {
        Page<ProductDTO> products = this.productService.getAll(PageRequest.of(page, limit), category);

        return ApiResponse.oK(
                ProductsPaginatedResponse.builder()
                        .products(products.getContent())
                        .totalPages(products.getTotalPages())
                        .totalItems(products.getNumberOfElements())
                        .build()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable(value = "id") long id) {
        return ApiResponse.oK(this.productService.getById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOne(@ModelAttribute ProductAddDTO productAddDTO) {
        return ApiResponse.oK(this.productService.createOne(productAddDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOne(@PathVariable(value = "id") long id,
                                                 @ModelAttribute ProductAddDTO body){
        return ApiResponse.oK(this.productService.updateById(id, body));
    }


   @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOne(@PathVariable(value = "id") long id){
        return ApiResponse.oK( this.productService.deleteById(id));
    }

}
