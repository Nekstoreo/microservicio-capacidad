package com.onclass.capacidad.application.usecase;

import com.onclass.capacidad.application.port.out.CapabilityRepositoryPort;
import com.onclass.capacidad.application.port.out.TechnologyCatalogPort;
import com.onclass.capacidad.application.usecase.command.CreateCapabilityCommand;
import com.onclass.capacidad.domain.exception.DomainValidationException;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.exception.TechnologiesNotFoundException;
import com.onclass.capacidad.domain.model.Capability;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCapabilityServiceTest {

    @Mock
    private CapabilityRepositoryPort capabilityRepositoryPort;

    @Mock
    private TechnologyCatalogPort technologyCatalogPort;

    @InjectMocks
    private CreateCapabilityService createCapabilityService;

    @Test
    void shouldCreateCapabilityWhenCommandIsValidAndDependenciesAreSatisfied() {
        CreateCapabilityCommand command = new CreateCapabilityCommand(
                "Backend",
                "Backend capability",
                List.of(1L, 2L, 3L)
        );
        Capability saved = Capability.rehydrate(10L, "Backend", "Backend capability", List.of(1L, 2L, 3L));

        when(capabilityRepositoryPort.existsByName("Backend")).thenReturn(false);
        when(technologyCatalogPort.findExistingTechnologyIds(Set.of(1L, 2L, 3L))).thenReturn(Set.of(1L, 2L, 3L));
        when(capabilityRepositoryPort.save(any(Capability.class))).thenReturn(saved);

        Capability result = createCapabilityService.execute(command);

        assertEquals(10L, result.getId());
        assertEquals("Backend", result.getName());
        verify(capabilityRepositoryPort).existsByName("Backend");
        verify(technologyCatalogPort).findExistingTechnologyIds(Set.of(1L, 2L, 3L));
        verify(capabilityRepositoryPort).save(any(Capability.class));
    }

    @Test
    void shouldThrowWhenCapabilityNameAlreadyExists() {
        CreateCapabilityCommand command = new CreateCapabilityCommand(
                "Backend",
                "Backend capability",
                List.of(1L, 2L, 3L)
        );

        when(capabilityRepositoryPort.existsByName("Backend")).thenReturn(true);

        assertThrows(DuplicateCapabilityException.class, () -> createCapabilityService.execute(command));
    }

    @Test
    void shouldThrowWhenTechnologiesDoNotExist() {
        CreateCapabilityCommand command = new CreateCapabilityCommand(
                "Backend",
                "Backend capability",
                List.of(1L, 2L, 3L)
        );

        when(capabilityRepositoryPort.existsByName("Backend")).thenReturn(false);
        when(technologyCatalogPort.findExistingTechnologyIds(Set.of(1L, 2L, 3L))).thenReturn(Set.of(1L, 3L));

        assertThrows(TechnologiesNotFoundException.class, () -> createCapabilityService.execute(command));
    }

    @Test
    void shouldThrowWhenTechnologyIdsContainDuplicates() {
        CreateCapabilityCommand command = new CreateCapabilityCommand(
                "Backend",
                "Backend capability",
                List.of(1L, 1L, 3L)
        );

        assertThrows(DomainValidationException.class, () -> createCapabilityService.execute(command));
    }
}
