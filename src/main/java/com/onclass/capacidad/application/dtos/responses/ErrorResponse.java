package com.onclass.capacidad.application.dtos.responses;

import java.time.Instant;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        String requestId
) {
    public ErrorResponse(String message) {
        this(Instant.now(), 0, null, message, null, null);
    }
}
