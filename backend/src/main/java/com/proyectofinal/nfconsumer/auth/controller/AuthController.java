package com.proyectofinal.nfconsumer.auth.controller;
import com.proyectofinal.nfconsumer.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "AUTH", description = "Endpoints de gestión de autenticación")
public class AuthController {

    private final AuthService service;

    @Operation(summary = "Registrar al usuario, obtenemos 2 tokens", security = @SecurityRequirement(name = "None"))
    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@RequestBody RegisterRequest request) {
        final TokenResponse response = service.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Logear al usuario, obtenemos 2 tokens", security = @SecurityRequirement(name = "None"))
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> authenticate(@RequestBody AuthRequest request) {
        final TokenResponse response = service.authenticate(request);
        return ResponseEntity.ok(response);
    }

    
    @Operation(summary = "Admite un Token, recibe otro nuevo y el refresh-token.", security = @SecurityRequirement(name = "None"))
    @PostMapping("/refresh-token")
    public TokenResponse refreshToken(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String authentication
    ) {
        return service.refreshToken(authentication);
    }


}