package com.proyectofinal.nfconsumer.balance.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.proyectofinal.nfconsumer.balance.repository.Balance;
import com.proyectofinal.nfconsumer.balance.repository.BalanceRepository;
import com.proyectofinal.nfconsumer.user.repository.User;
import com.proyectofinal.nfconsumer.user.repository.UserRepository;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

    public BalanceService(BalanceRepository balanceRepository, UserRepository userRepository) {
        this.balanceRepository = balanceRepository;
        this.userRepository = userRepository;
    }

    public BigDecimal getBalanceByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return balanceRepository.findByUser(user)
            .map(Balance::getActualBalance)
            .orElseThrow(() -> new RuntimeException("Pulsera no asignada al usuario"));
    }
}
