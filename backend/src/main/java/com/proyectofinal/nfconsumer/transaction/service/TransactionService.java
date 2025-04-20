package com.proyectofinal.nfconsumer.transaction.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyectofinal.nfconsumer.balance.repository.Balance;
import com.proyectofinal.nfconsumer.balance.repository.BalanceRepository;
import com.proyectofinal.nfconsumer.exceptions.NegativeBalanceException;
import com.proyectofinal.nfconsumer.exceptions.ResourceNotFoundException;
import com.proyectofinal.nfconsumer.exceptions.TransactionTypeNotRecognizedException;
import com.proyectofinal.nfconsumer.transaction.controller.TransactionHistoryResponse;
import com.proyectofinal.nfconsumer.transaction.controller.TransactionResponse;
import com.proyectofinal.nfconsumer.transaction.repository.Transaction;
import com.proyectofinal.nfconsumer.transaction.repository.TransactionRepository;
import com.proyectofinal.nfconsumer.transaction.repository.Transaction.TransactionType;
import com.proyectofinal.nfconsumer.user.repository.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;

    public TransactionResponse registerTransaction(User user, BigDecimal qty, TransactionType type, String description, String origin) {
        Balance balance = balanceRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Balance no encontrado para este usuario"));

        // Validar si es un pago y hay fondos suficientes
        if (type.equals(TransactionType.PAYMENT)) {
            if (balance.getActualBalance().compareTo(qty) < 0) {
                throw new NegativeBalanceException("Fondos insuficientes para realizar el pago");
            }
            balance.setActualBalance(balance.getActualBalance().subtract(qty));
        } else if (type.equals(TransactionType.RECHARGE)) {
            balance.setActualBalance(balance.getActualBalance().add(qty));
        } else {
            throw new TransactionTypeNotRecognizedException("Tipo de transacción inválido (PAYMENT // RECHARGE)");
        }

        // Guardar transacción
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setQty(qty);
        transaction.setType(type);
        transaction.setDescription(description);
        transaction.setOrigin(origin);
        transaction.setDate(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Actualizar balance
        balance.setUpdatedIn(LocalDateTime.now());
        balanceRepository.save(balance);

        return new TransactionResponse("Transacción registrada exitosamente");
    }

    public List<TransactionHistoryResponse> history(User user) {
        return transactionRepository.findAllByUserIdOrderByDateDesc(user.getId());
    }

    //private Balance getBalance(User user) {
    //    return balanceService.getBalanceByUser(user);
    //}

    //private void updateBalance(User user, BigDecimal newBalance) {
    //    balanceService.updateBalance(user, newBalance);
    //}

}
