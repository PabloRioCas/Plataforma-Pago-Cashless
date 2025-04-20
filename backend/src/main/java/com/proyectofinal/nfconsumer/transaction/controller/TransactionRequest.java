package com.proyectofinal.nfconsumer.transaction.controller;

import java.math.BigDecimal;
import com.proyectofinal.nfconsumer.transaction.repository.Transaction.TransactionType;

public record TransactionRequest(

    TransactionType type,
    BigDecimal qty,
    String description,
    String origin

) {
}
