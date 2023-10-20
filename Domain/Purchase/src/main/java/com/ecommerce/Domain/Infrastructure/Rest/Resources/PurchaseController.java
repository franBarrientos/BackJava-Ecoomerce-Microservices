package com.ecommerce.Domain.Infrastructure.Rest.Resources;

import com.ecommerce.Domain.Application.Dtos.OrderMpAddDTO;
import com.ecommerce.Domain.Application.Dtos.PurchaseAddDTO;
import com.ecommerce.Domain.Application.Dtos.PurchaseDTO;
import com.ecommerce.Domain.Application.Exceptions.BadRequest;
import com.ecommerce.Domain.Application.Exceptions.Unathorized;
import com.ecommerce.Domain.Application.Services.PurchaseService;
import com.ecommerce.Domain.Infrastructure.Rest.Config.ApiResponse;
import com.ecommerce.Domain.Infrastructure.Rest.Responses.PurchasePaginatedResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;


    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        Page<PurchaseDTO> purchases = this.purchaseService
                .getAllPurchases(PageRequest.of(page, limit));

        return ApiResponse.oK(
                PurchasePaginatedResponse.builder()
                        .purchases(purchases.getContent())
                        .totalPages(purchases.getTotalPages())
                        .totalItems(purchases.getNumberOfElements())
                        .build()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable(value = "id") long id ,
    @RequestHeader(value = "isAdmin", required = false) String isAdmin,
    @RequestHeader(value = "userId", required = false) Long userId) {

        if( isAdmin != null && isAdmin.equals("false") && userId != null &&
            !this.purchaseService.isOwnOfTheResource(id, userId)){
            throw new Unathorized("Access Denied");
        }
        return ApiResponse.oK(this.purchaseService.getById(id));
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<ApiResponse> getByCustomerId(@PathVariable(value = "id") long id ,
    @RequestHeader(value = "isAdmin", required = false) String isAdmin,
    @RequestHeader(value = "userId", required = false) Long userId) {

        if( isAdmin != null && isAdmin.equals("false") && userId != null &&
            !this.purchaseService.isOwnOfTheResourceCustomer(id, userId)){
            throw new Unathorized("Access Denied");
        }
        List<PurchaseDTO> purchases = this.purchaseService.getByCustomerId(id);
        return ApiResponse.oK(this.purchaseService.getByCustomerId(id));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> search(
            @RequestParam(value = "dni", required = false) Integer dni,
            @RequestParam(value = "firstName", required = false) String firstName,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int limit) {

        Page<PurchaseDTO> purchases = this.purchaseService
                .searchPurchases(dni, firstName, lastName, PageRequest.of(page, limit));

        return ApiResponse.oK(
                PurchasePaginatedResponse.builder()
                        .purchases(purchases.getContent())
                        .totalPages(purchases.getTotalPages())
                        .totalItems(purchases.getNumberOfElements())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOne(@Valid @RequestBody PurchaseAddDTO body) {
        return ApiResponse.oK(this.purchaseService.createOne(body));
    }
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse> getStadistics(
            @RequestParam(defaultValue = "0") int productPage,
            @RequestParam(defaultValue = "10") int productLimit,
            @RequestParam(defaultValue = "0") int categoryPage,
            @RequestParam(defaultValue = "10") int categoryLimit,
            @RequestParam(defaultValue = "0") int lastDaysPage,
            @RequestParam(defaultValue = "10") int lastDaysLimit) {
        return ApiResponse.oK(this.purchaseService
                .getStadistics(PageRequest.of(productPage, productLimit),
                        PageRequest.of(categoryPage, categoryLimit),
                        PageRequest.of(lastDaysPage, lastDaysLimit)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateOne(@PathVariable(value = "id") long id, @RequestBody PurchaseAddDTO body) {
        return ApiResponse.oK(this.purchaseService.updateById(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteOne(@PathVariable(value = "id") long id) {
        return ApiResponse.oK(this.purchaseService.deleteById(id));
    }

    @PostMapping("/create-order-mp")
    public ResponseEntity<ApiResponse> createOrderMp(@RequestBody OrderMpAddDTO body) {
        return ApiResponse.oK(this.purchaseService.createOrderMp(body));
    }

    @PostMapping("/webhook")
    public void recibeWebhookMp(
            @RequestParam String type,
            @RequestParam("data.id") Long dataId,
            @RequestBody Object body) {
        this.purchaseService.handleWebhook(type, dataId, body);
    }

}
