package com.onclass.capacidad.infrastructure.input.rest;

import com.onclass.capacidad.application.exception.TechnologyCatalogUnavailableException;
import com.onclass.capacidad.application.exception.TechnologyDeletionException;
import com.onclass.capacidad.domain.exception.CapabilityNotFoundException;
import com.onclass.capacidad.domain.exception.DomainValidationException;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.exception.TechnologiesNotFoundException;
import com.onclass.capacidad.infrastructure.constants.ApiConstants;
import com.onclass.capacidad.infrastructure.input.rest.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponse> handleDomainValidation(DomainValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(DuplicateCapabilityException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateCapabilityException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(TechnologiesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTechnologiesNotFound(TechnologiesNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(CapabilityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCapabilityNotFound(CapabilityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(TechnologyCatalogUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleTechnologyCatalogUnavailable(TechnologyCatalogUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(TechnologyDeletionException.class)
    public ResponseEntity<ErrorResponse> handleTechnologyDeletion(TechnologyDeletionException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(WebExchangeBindException ex) {
        FieldError fieldError = ex.getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : ApiConstants.DEFAULT_INVALID_REQUEST_MESSAGE;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(message));
    }
}
