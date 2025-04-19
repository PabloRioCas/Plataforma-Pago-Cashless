package com.proyectofinal.nfconsumer.exceptions;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BraceletNotAssignedException.class)
    public ResponseEntity<Map<String, String>> handleBraceletNotAssigned(BraceletNotAssignedException ex) {
        Map<String, String> response = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, String> response = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BraceletAlredyAssignedException.class)
    public ResponseEntity<Map<String, String>> handleBraceletAlredyAssignedException(BraceletAlredyAssignedException ex) {
        Map<String, String> response = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    //Si añadimos mas controlladores de las excepciones las lanzamos a continuación
}