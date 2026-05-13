package com.onclass.capacidad.domain.usecases;

import com.onclass.capacidad.application.dtos.CapabilityWithTechnologies;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.models.pagination.DomainPage;
import com.onclass.capacidad.domain.models.pagination.DomainPageRequest;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.spi.TechnologyCatalogPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCapabilitiesUseCaseTest {

    @Mock
    private CapabilityRepositoryPort capabilityRepositoryPort;

    @Mock
    private TechnologyCatalogPort technologyCatalogPort;

    @InjectMocks
    private ListCapabilitiesUseCase listCapabilitiesUseCase;

    @Test
    @DisplayName("Should return page of capabilities")
    void shouldReturnPageOfCapabilities() {
        DomainPageRequest pageRequest = new DomainPageRequest(0, 10, "name", "asc");
        DomainPage<Capability> expectedPage = new DomainPage<>(List.of(), 0, 10, 0, 0, false, false);
        when(capabilityRepositoryPort.findAll(pageRequest)).thenReturn(expectedPage);

        DomainPage<Capability> result = listCapabilitiesUseCase.execute(pageRequest);

        assertEquals(expectedPage, result);
        verify(capabilityRepositoryPort).findAll(pageRequest);
    }

    @Test
    @DisplayName("Should return page of capabilities with technology names")
    void shouldReturnPageOfCapabilitiesWithTechnologyNames() {
        DomainPageRequest pageRequest = new DomainPageRequest(0, 10, "name", "asc");
        Capability capability = Capability.rehydrate(1L, "Java", "Desc", List.of(101L, 102L, 103L));
        DomainPage<Capability> capabilityPage = new DomainPage<>(List.of(capability), 0, 10, 1, 1, false, false);

        when(capabilityRepositoryPort.findAll(pageRequest)).thenReturn(capabilityPage);
        when(technologyCatalogPort.findTechnologiesByIds(Set.of(101L, 102L, 103L)))
                .thenReturn(Map.of(101L, "Java Tech"));

        DomainPage<CapabilityWithTechnologies> result = listCapabilitiesUseCase.executeWithTechnologyNames(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.content().size());
        assertEquals("Java Tech", result.content().get(0).technologies().get(0).name());
    }

    @Test
    @DisplayName("Should return list of capabilities by ids with technology names")
    void shouldReturnListOfCapabilitiesByIdsWithTechnologyNames() {
        List<Long> ids = List.of(1L);
        Capability capability = Capability.rehydrate(1L, "Java", "Desc", List.of(101L, 102L, 103L));

        when(capabilityRepositoryPort.findAllByIds(ids)).thenReturn(List.of(capability));
        when(technologyCatalogPort.findTechnologiesByIds(Set.of(101L, 102L, 103L)))
                .thenReturn(Map.of(101L, "Java Tech"));

        List<CapabilityWithTechnologies> result = listCapabilitiesUseCase.getByIdsWithTechnologyNames(ids);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Java Tech", result.get(0).technologies().get(0).name());
    }
}
