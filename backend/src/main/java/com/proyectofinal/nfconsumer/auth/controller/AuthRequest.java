package com.proyectofinal.nfconsumer.auth.controller;

public record AuthRequest(
    String email,
    String password
    ) {}
