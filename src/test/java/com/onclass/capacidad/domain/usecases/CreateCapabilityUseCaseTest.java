package com.onclass.capacidad.domain.usecases;

import com.onclass.capacidad.application.dtos.commands.CreateCapabilityCommand;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.exception.TechnologiesNotFoundException;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.spi.TechnologyCatalogPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCapabilityUseCaseTest {

    @Mock
    private CapabilityRepositoryPort capabilityRepositoryPort;

    @Mock
    private TechnologyCatalogPort technologyCatalogPort;

    @InjectMocks
    private CreateCapabilityUseCase createCapabilityUseCase;

    @Test
    @DisplayName("Should create capability when command is valid")
    void shouldCreateCapabilityWhenCommandIsValid() {
        CreateCapabilityCommand command = new CreateCapabilityCommand("Java", "Java Description", List.of(1L, 2L, 3L));
        Capability expected = Capability.rehydrate(1L, "Java", "Java Description", List.of(1L, 2L, 3L));

        when(capabilityRepositoryPort.existsByName(anyString())).thenReturn(false);
        when(technologyCatalogPort.findExistingTechnologyIds(anySet())).thenReturn(Set.of(1L, 2L, 3L));
        when(capabilityRepositoryPort.save(any(Capability.class))).thenReturn(expected);

        Capability result = createCapabilityUseCase.execute(command);

        assertNotNull(result);
        assertEquals(expected.getName(), result.getName());
        verify(capabilityRepositoryPort).save(any(Capability.class));
    }

    @Test
    @DisplayName("Should throw DuplicateCapabilityException when name already exists")
    void shouldThrowDuplicateCapabilityExceptionWhenNameAlreadyExists() {
        CreateCapabilityCommand command = new CreateCapabilityCommand("Java", "Description", List.of(1L, 2L, 3L));

        when(capabilityRepositoryPort.existsByName("Java")).thenReturn(true);

        assertThrows(DuplicateCapabilityException.class, () -> createCapabilityUseCase.execute(command));
        verify(capabilityRepositoryPort, never()).save(any());
    }

    @Test
    @DisplayName("Should throw TechnologiesNotFoundException when some technologies do not exist")
    void shouldThrowTechnologiesNotFoundExceptionWhenSomeTechnologiesDoNotExist() {
        CreateCapabilityCommand command = new CreateCapabilityCommand("Java", "Description", List.of(1L, 2L, 3L));

        when(capabilityRepositoryPort.existsByName("Java")).thenReturn(false);
        when(technologyCatalogPort.findExistingTechnologyIds(Set.of(1L, 2L, 3L))).thenReturn(Set.of(1L, 2L));

        assertThrows(TechnologiesNotFoundException.class, () -> createCapabilityUseCase.execute(command));
        verify(capabilityRepositoryPort, never()).save(any());
    }
}
