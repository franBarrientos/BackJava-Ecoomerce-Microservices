package com.ecommerce.Domain.Infrastructure.Rest.Resources;

import com.ecommerce.Domain.Application.Dtos.CustomerDTO;
import com.ecommerce.Domain.Application.Exceptions.Unathorized;
import com.ecommerce.Domain.Application.Services.AuthService;
import com.ecommerce.Domain.Application.Services.CustomerService;
import com.ecommerce.Domain.Infrastructure.Rest.Config.ApiResponse;
import com.ecommerce.Domain.Infrastructure.Rest.Responses.CustomersPaginatedResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int limit) {

        Page<CustomerDTO> customerDTOPage = this.customerService.getAll(PageRequest.of(page, limit));

        return ApiResponse.oK(
                CustomersPaginatedResponse.builder()
                        .customers(customerDTOPage.getContent())
                        .totalItems(customerDTOPage.getNumberOfElements())
                        .totalPages(customerDTOPage.getTotalPages())
                        .build()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable(value = "id") long id,
        @RequestHeader(value = "isAdmin", required = false) String isAdmin,
        @RequestHeader(value = "userId", required = false) Long userId) {

        if (isAdmin != null && isAdmin.equals("false")
            && userId!= null
            && !this.customerService.isOwnOfTheResource(id,userId)) {

            throw new Unathorized("Access Denied");
        }
        return ApiResponse.oK(this.customerService.getById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse> getByUserId(@PathVariable(value = "id") long id,
    @RequestHeader(value = "isAdmin", required = false) String isAdmin,
    @RequestHeader(value = "userId", required = false) Long userId) {

        if(isAdmin != null && isAdmin.equals("false") && userId!= null && !(userId == id)){
            throw new Unathorized("Access Denied");
        }
        return ApiResponse.oK(this.customerService.getByUserId(id));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchCustomers(
    @RequestParam(value = "dni", required = false) Integer dni,
    @RequestParam(value = "firstName", required = false) String firstName,
    @RequestParam(value = "lastName", required = false) String lastName){

        return ApiResponse.oK(this.customerService.searchCustomers(dni, firstName, lastName));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody CustomerDTO body) {
        return ApiResponse.oK(this.customerService.createOne(body));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable(value = "id") Long id,
    @RequestBody CustomerDTO body,
    @RequestHeader(value = "isAdmin", required = false) String isAdmin,
    @RequestHeader(value = "userId", required = false) Long userId) {

        if (isAdmin != null && isAdmin.equals("false") &&
        userId != null && !this.customerService.isOwnOfTheResource(id,userId)) {
            throw new Unathorized("Access Denied");
        }

        return ApiResponse.oK(this.customerService.updateById(id, body));
    }
}
