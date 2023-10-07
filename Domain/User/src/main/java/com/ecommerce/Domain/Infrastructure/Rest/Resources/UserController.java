package com.ecommerce.Domain.Infrastructure.Rest.Resources;

import com.ecommerce.Domain.Application.Dtos.UserDTO;
import com.ecommerce.Domain.Application.Services.AuthService;
import com.ecommerce.Domain.Application.Services.UserService;
import com.ecommerce.Domain.Infrastructure.Rest.Config.ApiResponse;
import com.ecommerce.Domain.Infrastructure.Rest.Responses.UsersPaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse> getById(@PathVariable(value = "id") long id, Authentication authentication) {
        // if isn't admin and doesn't have the same id throw 403
        AuthService.checkIfAdminOrSameUser(id, authentication);
        return ApiResponse.oK(this.userService.getUserIsActive(id));

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ApiResponse> update(@PathVariable(value = "id") long id, @RequestBody UserDTO body, Authentication authentication) {
        // if isn't admin and doesn't have the same id throw 403
        AuthService.checkIfAdminOrSameUser(id, authentication);
        return ApiResponse.oK(this.userService.updateById(id, body));

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> delete(@PathVariable(value = "id") long id) {
        return ApiResponse.oK(this.userService.deleteById(id));
    }

}
