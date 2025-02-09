package com.proyectofinal.nfconsumer.user.controller;


import com.proyectofinal.nfconsumer.user.repository.UserRepository;
import com.proyectofinal.nfconsumer.user.repository.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "USER", description = "Endpoints de gestión de usuarios")
public class UserController {
    
    private final UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Necesita Token, Lista todos los usuario encontrado en la db, debería ser sustituido por el detalle del usuario.", security = @SecurityRequirement(name = "Bearer"))
    public List<UserResponse> changePassword() {
        
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(user.getName(), user.getEmail()))
                .toList();
    }

}
