package com.proyectofinal.nfconsumer.transaction.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.proyectofinal.nfconsumer.transaction.repository.Transaction;
import com.proyectofinal.nfconsumer.transaction.repository.Transaction.TransactionType;


public record TransactionHistoryResponse(
    TransactionType type,
    BigDecimal qty,
    String description,
    String origin,
    LocalDateTime date
) {
    public static TransactionHistoryResponse from(Transaction transaction) {
        
        return new TransactionHistoryResponse(
            transaction.getType(),
            transaction.getQty(),
            transaction.getDescription(),
            transaction.getOrigin(),
            transaction.getDate()
        );
    }
}