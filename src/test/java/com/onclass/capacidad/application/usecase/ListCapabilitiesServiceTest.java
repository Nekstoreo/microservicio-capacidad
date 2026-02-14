package com.onclass.capacidad.application.usecase;

import com.onclass.capacidad.application.dto.CapabilityWithTechnologies;
import com.onclass.capacidad.application.port.out.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.model.Capability;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCapabilitiesServiceTest {

    @Mock
    private CapabilityRepositoryPort capabilityRepositoryPort;

    @InjectMocks
    private ListCapabilitiesService listCapabilitiesService;

    @Test
    void shouldReturnPageOfCapabilitiesWhenPageableIsProvided() {
        Pageable pageable = PageRequest.of(0, 10);
        Capability capability1 = Capability.rehydrate(1L, "Backend", "Backend capability", List.of(1L, 2L, 3L));
        Capability capability2 = Capability.rehydrate(2L, "Frontend", "Frontend capability", List.of(4L, 5L, 6L));
        Page<Capability> expectedPage = new PageImpl<>(List.of(capability1, capability2), pageable, 2);

        when(capabilityRepositoryPort.findAll(pageable)).thenReturn(expectedPage);

        Page<Capability> result = listCapabilitiesService.execute(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Backend", result.getContent().get(0).getName());
        assertEquals("Frontend", result.getContent().get(1).getName());
        verify(capabilityRepositoryPort).findAll(pageable);
    }

    @Test
    void shouldReturnEmptyPageWhenNoCapabilitiesExist() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Capability> expectedPage = new PageImpl<>(List.of(), pageable, 0);

        when(capabilityRepositoryPort.findAll(pageable)).thenReturn(expectedPage);

        Page<Capability> result = listCapabilitiesService.execute(pageable);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        verify(capabilityRepositoryPort).findAll(pageable);
    }

    @Test
    void shouldReturnCapabilitiesWithTechnologyNamesWhenRequested() {
        Pageable pageable = PageRequest.of(0, 10);
        CapabilityWithTechnologies capWithTechs = new CapabilityWithTechnologies(
                1L,
                "Backend",
                "Backend capability",
                List.of(
                        new CapabilityWithTechnologies.TechnologyInfo(1L, "Java"),
                        new CapabilityWithTechnologies.TechnologyInfo(2L, "Spring")
                )
        );
        Page<CapabilityWithTechnologies> expectedPage = new PageImpl<>(List.of(capWithTechs), pageable, 1);

        when(capabilityRepositoryPort.findAllWithTechnologyNames(pageable)).thenReturn(expectedPage);

        Page<CapabilityWithTechnologies> result = listCapabilitiesService.executeWithTechnologyNames(pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Backend", result.getContent().get(0).name());
        assertEquals(2, result.getContent().get(0).technologies().size());
        assertEquals("Java", result.getContent().get(0).technologies().get(0).name());
        verify(capabilityRepositoryPort).findAllWithTechnologyNames(pageable);
    }

    @Test
    void shouldHandleMultiplePages() {
        Pageable pageable = PageRequest.of(1, 5);
        Capability capability1 = Capability.rehydrate(6L, "DevOps", "DevOps capability", List.of(7L, 8L, 9L));
        Page<Capability> expectedPage = new PageImpl<>(List.of(capability1), pageable, 12);

        when(capabilityRepositoryPort.findAll(pageable)).thenReturn(expectedPage);

        Page<Capability> result = listCapabilitiesService.execute(pageable);

        assertNotNull(result);
        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(12, result.getTotalElements());
        assertTrue(result.hasNext());
        assertFalse(result.isFirst());
        verify(capabilityRepositoryPort).findAll(pageable);
    }
}
