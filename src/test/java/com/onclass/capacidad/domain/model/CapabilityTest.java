package com.onclass.capacidad.domain.model;

import com.onclass.capacidad.domain.exception.DomainValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityTest {

    @Test
    @DisplayName("Should create capability when all fields are valid")
    void shouldCreateCapabilityWhenAllFieldsAreValid() {
        String name = "Backend";
        String description = "Backend technologies";
        List<Long> technologyIds = List.of(1L, 2L, 3L);

        Capability capability = Capability.create(name, description, technologyIds);

        assertNotNull(capability);
        assertEquals(name, capability.getName());
        assertEquals(description, capability.getDescription());
        assertEquals(technologyIds, capability.getTechnologyIds());
        assertNull(capability.getId());
    }

    @Test
    @DisplayName("Should rehydrate capability with id")
    void shouldRehydrateCapabilityWithId() {
        Long id = 1L;
        String name = "Frontend";
        String description = "Frontend technologies";
        List<Long> technologyIds = List.of(1L, 2L, 3L);

        Capability capability = Capability.rehydrate(id, name, description, technologyIds);

        assertEquals(id, capability.getId());
        assertEquals(name, capability.getName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"  ", "\t", "\n"})
    @DisplayName("Should throw exception when name is invalid")
    void shouldThrowExceptionWhenNameIsInvalid(String invalidName) {
        List<Long> technologyIds = List.of(1L, 2L, 3L);
        assertThrows(DomainValidationException.class,
                () -> Capability.create(invalidName, "Description", technologyIds));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Should throw exception when description is invalid")
    void shouldThrowExceptionWhenDescriptionIsInvalid(String invalidDescription) {
        List<Long> technologyIds = List.of(1L, 2L, 3L);
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", invalidDescription, technologyIds));
    }

    @Test
    @DisplayName("Should throw exception when technologyIds is null")
    void shouldThrowExceptionWhenTechnologyIdsIsNull() {
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", null));
    }

    @Test
    @DisplayName("Should throw exception when technologyIds is empty")
    void shouldThrowExceptionWhenTechnologyIdsIsEmpty() {
        List<Long> technologyIds = List.of();
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", technologyIds));
    }

    @Test
    @DisplayName("Should throw exception when technologyIds is less than 3")
    void shouldThrowExceptionWhenTechnologyIdsIsLessThanThree() {
        List<Long> technologyIds = List.of(1L, 2L);
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", technologyIds));
    }

    @Test
    @DisplayName("Should throw exception when technologyIds is more than 20")
    void shouldThrowExceptionWhenTechnologyIdsIsMoreThanTwenty() {
        List<Long> tooManyIds = Arrays.asList(
                1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L,
                11L, 12L, 13L, 14L, 15L, 16L, 17L, 18L, 19L, 20L, 21L);
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", tooManyIds));
    }

    @Test
    @DisplayName("Should throw exception when technologyId is null")
    void shouldThrowExceptionWhenTechnologyIdIsNull() {
        List<Long> technologyIds = Arrays.asList(1L, 2L, null);
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", technologyIds));
    }

    @Test
    @DisplayName("Should throw exception when technologyId is zero")
    void shouldThrowExceptionWhenTechnologyIdIsZero() {
        List<Long> technologyIds = List.of(101L, 102L, 0L);
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", technologyIds));
    }

    @Test
    @DisplayName("Should throw exception when technologyId is negative")
    void shouldThrowExceptionWhenTechnologyIdIsNegative() {
        List<Long> technologyIds = List.of(101L, 102L, -1L);
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", technologyIds));
    }

    @Test
    @DisplayName("Should throw exception when technologyIds contains duplicates")
    void shouldThrowExceptionWhenTechnologyIdsContainsDuplicates() {
        List<Long> technologyIds = List.of(1L, 2L, 1L);
        assertThrows(DomainValidationException.class,
                () -> Capability.create("Backend", "Description", technologyIds));
    }

    @Test
    @DisplayName("Should normalize name and description")
    void shouldNormalizeNameAndDescription() {
        Capability capability = Capability.create("  Backend  ", "  Description  ", List.of(1L, 2L, 3L));
        assertEquals("Backend", capability.getName());
        assertEquals("Description", capability.getDescription());
    }
}
