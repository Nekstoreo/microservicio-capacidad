package com.onclass.capacidad.domain.usecases;

import com.onclass.capacidad.domain.exception.CapabilityNotFoundException;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.spi.TechnologyManagementPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCapabilityUseCaseTest {

    @Mock
    private CapabilityRepositoryPort capabilityRepositoryPort;

    @Mock
    private TechnologyManagementPort technologyManagementPort;

    @InjectMocks
    private DeleteCapabilityUseCase deleteCapabilityUseCase;

    @Test
    @DisplayName("Should delete capability and unused technologies")
    void shouldDeleteCapabilityAndUnusedTechnologies() {
        Long capabilityId = 1L;
        List<Long> techIds = List.of(101L, 102L, 103L);
        Capability capability = Capability.rehydrate(capabilityId, "Java", "Desc", techIds);

        when(capabilityRepositoryPort.findById(capabilityId)).thenReturn(capability);
        // tech 101 is still referenced elsewhere, tech 102 is not
        when(capabilityRepositoryPort.findTechnologyReferences(techIds)).thenReturn(Set.of(101L));

        deleteCapabilityUseCase.execute(capabilityId);

        verify(capabilityRepositoryPort).deleteById(capabilityId);
        verify(technologyManagementPort, never()).deleteTechnology(101L);
        verify(technologyManagementPort).deleteTechnology(102L);
    }

    @Test
    @DisplayName("Should throw CapabilityNotFoundException when capability does not exist")
    void shouldThrowCapabilityNotFoundExceptionWhenCapabilityDoesNotExist() {
        Long capabilityId = 1L;
        when(capabilityRepositoryPort.findById(capabilityId)).thenReturn(null);

        assertThrows(CapabilityNotFoundException.class, () -> deleteCapabilityUseCase.execute(capabilityId));
        verify(capabilityRepositoryPort, never()).deleteById(anyLong());
    }
}
