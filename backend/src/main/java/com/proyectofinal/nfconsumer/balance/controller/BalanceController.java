package com.proyectofinal.nfconsumer.balance.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectofinal.nfconsumer.balance.service.BalanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/balance")
@RequiredArgsConstructor
@Tag(name = "USER", description = "Endpoints de gestión de usuarios")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    @Operation(summary = "Necesita Token, Muestra el balance del usuario comprobando el username.", security = @SecurityRequirement(name = "Bearer"))
    public ResponseEntity<Map<String, BigDecimal>> getBalance(Authentication authentication) {
        String email = authentication.getName();
        BigDecimal balance = balanceService.getBalanceByEmail(email);
        Map<String, BigDecimal> response = Map.of("balance", balance);
        return ResponseEntity.ok(response);
    }   

}