package com.onclass.capacidad.application.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApplicationExceptionsTest {

    @Test
    @DisplayName("TechnologyCatalogUnavailableException should store message")
    void technologyCatalogUnavailableExceptionTest() {
        String message = "Service unavailable";
        TechnologyCatalogUnavailableException ex = new TechnologyCatalogUnavailableException(message);
        assertEquals(message, ex.getMessage());
    }

    @Test
    @DisplayName("TechnologyDeletionException should store message")
    void technologyDeletionExceptionTest() {
        String message = "Deletion failed";
        TechnologyDeletionException ex = new TechnologyDeletionException(message);
        assertEquals(message, ex.getMessage());
    }
}
