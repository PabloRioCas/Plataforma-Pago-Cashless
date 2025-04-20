package com.proyectofinal.nfconsumer.auth.controller;

public record RegisterRequest(
    String email,
    String password,
    String name,
    String lastname
    ) {}
