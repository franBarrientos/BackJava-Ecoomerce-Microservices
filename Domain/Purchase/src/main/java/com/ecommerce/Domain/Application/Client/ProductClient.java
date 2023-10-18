package com.ecommerce.Domain.Application.Client;

import com.ecommerce.Domain.Application.Dtos.ProductDTO;

public interface ProductClient {

    ProductDTO getProductDTO(Long productId);


}
