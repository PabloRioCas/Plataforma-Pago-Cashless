package com.proyectofinal.nfconsumer.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController{
    
    @GetMapping
    public String getAllPayments(){
        return "Lista de pagos";
    };

}
