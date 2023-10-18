package com.ecommerce.Domain.Infrastructure.Rest.Resources;

import com.ecommerce.Domain.Application.Dtos.UserDTO;
import com.ecommerce.Domain.Application.Exceptions.BadRequest;
import com.ecommerce.Domain.Application.Exceptions.Unathorized;
import com.ecommerce.Domain.Application.Services.AuthService;
import com.ecommerce.Domain.Application.Services.UserService;
import com.ecommerce.Domain.Infrastructure.Rest.Config.ApiResponse;
import com.ecommerce.Domain.Infrastructure.Rest.Responses.UsersPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int limit) {

        Page<UserDTO> userDTOS = this.userService.getAllUsers(PageRequest.of(page, limit));

        return ApiResponse.oK(
                UsersPaginatedResponse.builder()
                        .users(userDTOS.getContent())
                        .totalItems(userDTOS.getNumberOfElements())
                        .totalPages(userDTOS.getTotalPages())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable(value = "id") long id,
    @RequestHeader(value = "isAdmin", required = false) String isAdmin,
    @RequestHeader(value = "userId", required = false) Long userId) {

        if(isAdmin != null && isAdmin.equals("false") && userId != null && !(userId == id)){
            throw new Unathorized("Access Denied");
        }

        return ApiResponse.oK(this.userService.getUserIsActive(id));

    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable(value = "id") long id,
    @RequestBody UserDTO body ,
    @RequestHeader(value = "isAdmin", required = false) String isAdmin,
    @RequestHeader(value = "userId", required = false) Long userId) {

        if(isAdmin != null && isAdmin.equals("false") && userId != null && !(userId == id)){
            throw new Unathorized("Access Denied");
        }
        return ApiResponse.oK(this.userService.updateById(id, body));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable(value = "id") long id) {
        return ApiResponse.oK(this.userService.deleteById(id));
    }

}
