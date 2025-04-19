package com.proyectofinal.nfconsumer.bracelet.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectofinal.nfconsumer.bracelet.service.SyncService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sync")
@RequiredArgsConstructor
public class SyncController {

    private final SyncService syncService;

    @Operation(summary = "Registrar la pulsera del usuario e inicializar el saldo a 0", security = @SecurityRequirement(name = "Bearer"))
    @PostMapping
    public ResponseEntity<SyncResponse> syncBracelet(
        @RequestBody SyncRequest request,
        Authentication authentication) {
            System.out.println(request);
        return ResponseEntity.ok(syncService.syncBracelet(authentication.getName(), request.nfcUid()));
    }
}