package com.proyectofinal.nfconsumer.bracelet.controller;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SyncResponse (
        @JsonProperty("sync_status")
        String sync_status
) {
}