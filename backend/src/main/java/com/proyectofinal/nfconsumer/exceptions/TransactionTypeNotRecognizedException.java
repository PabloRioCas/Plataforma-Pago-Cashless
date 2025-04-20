package com.proyectofinal.nfconsumer.exceptions;

public class TransactionTypeNotRecognizedException extends RuntimeException{
    public TransactionTypeNotRecognizedException(String message) {
        super(message);
    }
}
