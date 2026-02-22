package com.onclass.capacidad.infrastructure.mappers;

import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.infrastructure.entities.CapabilityEntity;
import com.onclass.capacidad.infrastructure.entities.CapabilityTechnologyEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapabilityEntityMapperTest {

    private final CapabilityEntityMapper mapper = new CapabilityEntityMapper();

    @Test
    @DisplayName("Should map Capability domain to CapabilityEntity")
    void shouldMapToEntity() {
        Capability capability = Capability.rehydrate(1L, "Java", "Desc", List.of(101L, 102L, 103L));

        CapabilityEntity entity = mapper.toEntity(capability);

        assertNotNull(entity);
        assertEquals("Java", entity.getName());
        assertEquals("Desc", entity.getDescription());
    }

    @Test
    @DisplayName("Should return null when domain is null")
    void shouldReturnNullWhenDomainIsNull() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should map CapabilityEntity to Capability domain")
    void shouldMapToDomain() {
        CapabilityEntity entity = new CapabilityEntity();
        entity.setId(1L);
        entity.setName("Java");
        entity.setDescription("Desc");

        CapabilityTechnologyEntity tech1 = new CapabilityTechnologyEntity();
        tech1.setTechnologyId(101L);
        CapabilityTechnologyEntity tech2 = new CapabilityTechnologyEntity();
        tech2.setTechnologyId(102L);
        CapabilityTechnologyEntity tech3 = new CapabilityTechnologyEntity();
        tech3.setTechnologyId(103L);
        entity.setTechnologies(List.of(tech1, tech2, tech3));

        Capability domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("Java", domain.getName());
        assertEquals(3, domain.getTechnologyIds().size());
        assertEquals(101L, domain.getTechnologyIds().get(0));
    }

    @Test
    @DisplayName("Should return null when entity is null")
    void shouldReturnNullWhenEntityIsNull() {
        assertNull(mapper.toDomain(null));
    }
}
