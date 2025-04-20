package com.proyectofinal.nfconsumer.transaction.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TransactionResponse (

    @JsonProperty("transaction_state")
    String transaction_state

){
}

