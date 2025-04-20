package com.proyectofinal.nfconsumer.balance.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.proyectofinal.nfconsumer.balance.repository.Balance;
import com.proyectofinal.nfconsumer.balance.repository.BalanceRepository;
import com.proyectofinal.nfconsumer.exceptions.BraceletNotAssignedException;
import com.proyectofinal.nfconsumer.user.repository.User;
import com.proyectofinal.nfconsumer.user.repository.UserRepository;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final UserRepository userRepository;

    private BalanceService(BalanceRepository balanceRepository, UserRepository userRepository) {
        this.balanceRepository = balanceRepository;
        this.userRepository = userRepository;
    }

    public BigDecimal getBalanceByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new BraceletNotAssignedException("OOPS! Parece que el usuario no existe"));

        return balanceRepository.findByUser(user)
            .map(Balance::getActualBalance)
            .orElseThrow(() -> new BraceletNotAssignedException("Este usuario aun no tiene una pulsera asignada, por lo que no puede tener balance"));
    }

    public void firstAssignBalance(User user){
        balanceRepository.findByUser(user).orElseGet(() -> {
            Balance balance = new Balance();
            balance.setUser(user);
            balance.setActualBalance(BigDecimal.ZERO);
            balance.setUpdatedIn(LocalDateTime.now());
            return balanceRepository.save(balance);
        });

    }
}
