package com.onclass.capacidad.infrastructure.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ApplicationPropertiesTest {

    @Test
    @DisplayName("Should set and get application properties")
    void testApplicationProperties() {
        ApplicationProperties properties = new ApplicationProperties();

        properties.getTechnology().setBaseUrl("http://localhost:8080");
        properties.getSecurity().setEnabled(false);
        properties.getHttpClient().getCredentials().setUsername("user");
        properties.getHttpClient().getCredentials().setPassword("pass");

        assertEquals("http://localhost:8080", properties.getTechnology().getBaseUrl());
        assertFalse(properties.getSecurity().isEnabled());
        assertEquals("user", properties.getHttpClient().getCredentials().getUsername());
        assertEquals("pass", properties.getHttpClient().getCredentials().getPassword());
    }
}
