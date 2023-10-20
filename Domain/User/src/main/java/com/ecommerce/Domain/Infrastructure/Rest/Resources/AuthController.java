package com.ecommerce.Domain.Infrastructure.Rest.Resources;

import com.ecommerce.Domain.Application.Dtos.LoginRequest;
import com.ecommerce.Domain.Application.Dtos.RegisterRequest;
import com.ecommerce.Domain.Application.Services.AuthService;
import com.ecommerce.Domain.Infrastructure.Rest.Config.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest body) {
        return ApiResponse.create(authService.register(body));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest body) {
        return ApiResponse.oK(authService.login(body));

    }
    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse> refreshToken(
    @RequestHeader("Authorization") String token) {
        return ApiResponse.oK(authService.refreshToken(token));

    }

    @PostMapping("/google")
    public ResponseEntity<ApiResponse> loginGoogle(@RequestBody Map body) {
        return ApiResponse.oK(authService.loginGoogle(body));
    }
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(authService.getRoles(token));
    }

}
