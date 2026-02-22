package com.onclass.capacidad.application.mappers;

import com.onclass.capacidad.application.dtos.CapabilityWithTechnologies;
import com.onclass.capacidad.application.dtos.responses.CapabilityListItemResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CapabilityMapperTest {

    private final CapabilityMapper capabilityMapper = new CapabilityMapper();

    @Test
    @DisplayName("Should map CapabilityWithTechnologies to CapabilityListItemResponse")
    void shouldMapToListItemResponse() {
        CapabilityWithTechnologies.TechnologyInfo techInfo = new CapabilityWithTechnologies.TechnologyInfo(101L,
                "Java");
        CapabilityWithTechnologies dto = new CapabilityWithTechnologies(1L, "Backend", "Desc", List.of(techInfo));

        CapabilityListItemResponse result = capabilityMapper.toListItemResponse(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Backend", result.name());
        assertEquals("Desc", result.description());
        assertEquals(1, result.technologies().size());
        assertEquals(101L, result.technologies().get(0).id());
        assertEquals("Java", result.technologies().get(0).name());
    }
}
