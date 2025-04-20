package com.proyectofinal.nfconsumer.transaction.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectofinal.nfconsumer.transaction.service.TransactionService;
import com.proyectofinal.nfconsumer.user.repository.User;
import com.proyectofinal.nfconsumer.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactions;
    //private final UserService userService;
    private final UserRepository userRepository;

    //REVISAR BUG LOOP INFINITO.
    @GetMapping
    public ResponseEntity<List<TransactionHistoryResponse>> getHistory(Authentication authentication) {
        
        // Esto se debe refactorizar para que UserService se encargue de buscar usuarios y no repitamos código.
        //User user = userService.resolve(authentication);
        //return ResponseEntity.ok(transactions.history(user));

        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        final List<TransactionHistoryResponse> response = transactions.history(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> registerPayment(
        @RequestBody TransactionRequest request,
        Authentication authentication) {
        
        //User user = userService.resolve(authentication);
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        final TransactionResponse response = transactions.registerTransaction(
            user,
            request.qty(),
            request.type(),
            request.description(),
            request.origin()
        );
        return ResponseEntity.ok(response);
    }

} 