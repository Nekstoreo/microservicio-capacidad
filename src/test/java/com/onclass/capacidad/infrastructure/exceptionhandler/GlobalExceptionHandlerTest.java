package com.onclass.capacidad.infrastructure.exceptionhandler;

import com.onclass.capacidad.application.dtos.responses.ErrorResponse;
import com.onclass.capacidad.application.exception.TechnologyCatalogUnavailableException;
import com.onclass.capacidad.application.exception.TechnologyDeletionException;
import com.onclass.capacidad.domain.exception.CapabilityNotFoundException;
import com.onclass.capacidad.domain.exception.DomainValidationException;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.exception.TechnologiesNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle DomainValidationException")
    void handleDomainValidation() {
        ResponseEntity<ErrorResponse> response = handler
                .handleDomainValidation(new DomainValidationException("Invalid"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle DuplicateCapabilityException")
    void handleDuplicate() {
        ResponseEntity<ErrorResponse> response = handler.handleDuplicate(new DuplicateCapabilityException("Java"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle TechnologiesNotFoundException")
    void handleTechnologiesNotFound() {
        ResponseEntity<ErrorResponse> response = handler
                .handleTechnologiesNotFound(new TechnologiesNotFoundException(List.of(1L)));
        assertEquals(HttpStatus.UNPROCESSABLE_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle CapabilityNotFoundException")
    void handleCapabilityNotFound() {
        ResponseEntity<ErrorResponse> response = handler.handleCapabilityNotFound(new CapabilityNotFoundException(1L));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle TechnologyCatalogUnavailableException")
    void handleTechnologyCatalogUnavailable() {
        ResponseEntity<ErrorResponse> response = handler
                .handleTechnologyCatalogUnavailable(new TechnologyCatalogUnavailableException("Error"));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle TechnologyDeletionException")
    void handleTechnologyDeletion() {
        ResponseEntity<ErrorResponse> response = handler
                .handleTechnologyDeletion(new TechnologyDeletionException("Error"));
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    }

    @Test
    @DisplayName("Should handle WebExchangeBindException")
    void handleBindException() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        FieldError fieldError = new FieldError("object", "field", "default message");
        when(ex.getFieldError()).thenReturn(fieldError);

        ResponseEntity<ErrorResponse> response = handler.handleBindException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("default message", response.getBody().message());
    }
}
