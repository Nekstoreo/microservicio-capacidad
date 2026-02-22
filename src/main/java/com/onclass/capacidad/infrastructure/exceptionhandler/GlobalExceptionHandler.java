package com.onclass.capacidad.infrastructure.exceptionhandler;

import com.onclass.capacidad.application.dtos.responses.ErrorResponse;
import com.onclass.capacidad.application.exception.TechnologyCatalogUnavailableException;
import com.onclass.capacidad.application.exception.TechnologyDeletionException;
import com.onclass.capacidad.domain.exception.CapabilityNotFoundException;
import com.onclass.capacidad.domain.exception.DomainValidationException;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.exception.TechnologiesNotFoundException;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidation(DomainValidationException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), exchange);
    }
    public ResponseEntity<ErrorResponse> handleDomainValidation(DomainValidationException ex) {
        return handleDomainValidation(ex, null);
    }

    @ExceptionHandler(DuplicateCapabilityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateCapabilityException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), exchange);
    }
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateCapabilityException ex) {
        return handleDuplicate(ex, null);
    }

    @ExceptionHandler(TechnologiesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTechnologiesNotFound(TechnologiesNotFoundException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.UNPROCESSABLE_CONTENT, ex.getMessage(), exchange);
    }
    public ResponseEntity<ErrorResponse> handleTechnologiesNotFound(TechnologiesNotFoundException ex) {
        return handleTechnologiesNotFound(ex, null);
    }

    @ExceptionHandler(CapabilityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCapabilityNotFound(CapabilityNotFoundException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), exchange);
    }
    public ResponseEntity<ErrorResponse> handleCapabilityNotFound(CapabilityNotFoundException ex) {
        return handleCapabilityNotFound(ex, null);
    }

    @ExceptionHandler(TechnologyCatalogUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleTechnologyCatalogUnavailable(TechnologyCatalogUnavailableException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), exchange);
    }
    public ResponseEntity<ErrorResponse> handleTechnologyCatalogUnavailable(TechnologyCatalogUnavailableException ex) {
        return handleTechnologyCatalogUnavailable(ex, null);
    }

    @ExceptionHandler(TechnologyDeletionException.class)
    public ResponseEntity<ErrorResponse> handleTechnologyDeletion(TechnologyDeletionException ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), exchange);
    }
    public ResponseEntity<ErrorResponse> handleTechnologyDeletion(TechnologyDeletionException ex) {
        return handleTechnologyDeletion(ex, null);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(WebExchangeBindException ex, ServerWebExchange exchange) {
        FieldError fieldError = ex.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : ApiConstants.DEFAULT_INVALID_REQUEST_MESSAGE;
        return buildError(HttpStatus.BAD_REQUEST, message, exchange);
    }
    public ResponseEntity<ErrorResponse> handleBindException(WebExchangeBindException ex) {
        return handleBindException(ex, null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, ServerWebExchange exchange) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", exchange);
    }

    private ResponseEntity<ErrorResponse> buildError(HttpStatus status, String message, ServerWebExchange exchange) {
        String path = exchange != null ? exchange.getRequest().getPath().value() : null;
        String requestId = exchange != null ? exchange.getRequest().getId() : null;
        return ResponseEntity.status(status).body(new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                requestId
        ));
    }
}
