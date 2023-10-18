package com.ecommerce.Domain.Application.Mappers;

import com.ecommerce.Domain.Application.Client.ProductClient;
import com.ecommerce.Domain.Application.Client.UserClient;
import com.ecommerce.Domain.Application.Dtos.CustomerDTO;
import com.ecommerce.Domain.Application.Dtos.ProductDTO;
import com.ecommerce.Domain.Application.Dtos.PurchaseDTO;
import com.ecommerce.Domain.Application.Dtos.PurchaseProductDTO;
import com.ecommerce.Domain.Application.Exceptions.NotFoundException;
import com.ecommerce.Domain.Domain.Purchase;
import com.ecommerce.Domain.Domain.PurchaseProduct;
import jakarta.ws.rs.ServerErrorException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class PurchaseDtoMapper {

    @Autowired
    private  UserClient userClient;

    @Autowired
    private  ProductClient productClient;
    public PurchaseDTO toDto(Purchase purchase){
        if ( purchase == null ) {
            return null;
        }


        CompletableFuture<CustomerDTO> customerFuture =
                CompletableFuture.supplyAsync(() -> this.customerIdToCustomerDTO(purchase.getCustomerId()));

        CompletableFuture<List<PurchaseProductDTO>> purchaseProductsFuture = CompletableFuture.supplyAsync(() ->
                purchase.getPurchaseProducts()
                        .stream()
                        .map(this::purchaseProductToPurchaseProductDTO)
                        .collect(Collectors.toList()));


        PurchaseDTO.PurchaseDTOBuilder purchaseDTO = PurchaseDTO.builder();

        purchaseDTO.totalSale( this.calculateTotalSale( purchase ) );
        purchaseDTO.id( purchase.getId() );
        purchaseDTO.payment( purchase.getPayment() );
        purchaseDTO.state( purchase.getState() );
        purchaseDTO.createdAt( purchase.getCreatedAt() );
        purchaseDTO.updatedAt( purchase.getUpdatedAt() );

        CompletableFuture<Void> combinedFuture = customerFuture.thenCombine(purchaseProductsFuture, (customer, pps) -> {
            purchaseDTO.customer(customer);
            purchaseDTO.purchaseProducts(pps);
            return null;
        });
        combinedFuture.join();
        return purchaseDTO.build();
    }
    public PurchaseDTO toDto(Purchase purchase, CustomerDTO customerDTO) {
        if ( purchase == null ) {
            return null;
        }

        CompletableFuture<List<PurchaseProductDTO>> purchaseProductsFuture = CompletableFuture.supplyAsync(() ->
                purchase.getPurchaseProducts()
                        .stream()
                        .map(this::purchaseProductToPurchaseProductDTO)
                        .collect(Collectors.toList()));


        PurchaseDTO.PurchaseDTOBuilder purchaseDTO = PurchaseDTO.builder();

        purchaseDTO.totalSale( this.calculateTotalSale( purchase ) );
        purchaseDTO.id( purchase.getId() );
        purchaseDTO.payment( purchase.getPayment() );
        purchaseDTO.state( purchase.getState() );
        purchaseDTO.createdAt( purchase.getCreatedAt() );
        purchaseDTO.updatedAt( purchase.getUpdatedAt() );
        purchaseDTO.customer(customerDTO);
        try {
            purchaseDTO.purchaseProducts(purchaseProductsFuture.get());
        }catch (Exception e){
            throw new ServerErrorException(e.getMessage(), 500);
        }
        return purchaseDTO.build();
    }

    @Mapping(target = "product", source = "productId", qualifiedByName = "productIdToProductDTO")
     public abstract PurchaseProductDTO purchaseProductToPurchaseProductDTO(PurchaseProduct purchaseProduct);

    @Named("productIdToProductDTO")
    public ProductDTO productIdToProductDTO(Long productId) {
        ProductDTO productDTO = this.productClient.getProductDTO(productId);
        if (productDTO == null) {
            throw new NotFoundException("product " + productId + " not found");
        }
        return productDTO;
    }

    @Named("customerIdToCustomerDTO")
    public CustomerDTO customerIdToCustomerDTO(Long customerId) {
        CustomerDTO customerDTO = this.userClient.getCustomerDTO(customerId);
        if (customerDTO == null) {
            throw new NotFoundException("customer " + customerId + " not found");
        }
        return customerDTO;
    }


    @Named("calculateTotalSale")
    public BigDecimal calculateTotalSale(Purchase purchase) {
        return purchase.getPurchaseProducts()
                .stream()
                .map(pp -> pp.getTotalPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    };

    public abstract Purchase toDomain(PurchaseDTO purchaseDTO);

}
