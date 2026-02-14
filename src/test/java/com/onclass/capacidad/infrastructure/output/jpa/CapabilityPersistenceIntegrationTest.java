package com.onclass.capacidad.infrastructure.output.jpa;

import com.onclass.capacidad.application.port.out.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.infrastructure.output.jpa.adapter.CapabilityJpaAdapter;
import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityEntity;
import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityTechnologyEntity;
import com.onclass.capacidad.infrastructure.output.jpa.repository.CapabilityJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(CapabilityJpaAdapter.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:capability;MODE=MySQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.data.jpa.repositories.enabled=true",
        "spring.flyway.enabled=false"
})
class CapabilityPersistenceIntegrationTest {

    @Autowired
    private CapabilityRepositoryPort capabilityRepositoryPort;

    @Autowired
    private CapabilityJpaRepository capabilityJpaRepository;

    @Test
    void shouldPersistCapabilityAndTechnologyAssociation() {
        Capability capability = Capability.create("Backend", "Backend capability", List.of(1L, 2L, 3L));

        Capability saved = capabilityRepositoryPort.save(capability);

        assertNotNull(saved.getId());
        assertEquals("Backend", saved.getName());
        assertEquals(3, saved.getTechnologyIds().size());
        assertTrue(capabilityRepositoryPort.existsByName("Backend"));
    }

    @Test
    void shouldEnforceUniqueCapabilityNameAtDatabaseLevel() {
        CapabilityEntity first = new CapabilityEntity();
        first.setName("Backend");
        first.setDescription("Backend capability");
        first.addTechnology(technology(1L));
        first.addTechnology(technology(2L));
        first.addTechnology(technology(3L));
        capabilityJpaRepository.saveAndFlush(first);

        CapabilityEntity duplicate = new CapabilityEntity();
        duplicate.setName("Backend");
        duplicate.setDescription("Another capability");
        duplicate.addTechnology(technology(4L));
        duplicate.addTechnology(technology(5L));
        duplicate.addTechnology(technology(6L));

        assertThrows(DataIntegrityViolationException.class, () -> capabilityJpaRepository.saveAndFlush(duplicate));
    }

    private CapabilityTechnologyEntity technology(Long technologyId) {
        CapabilityTechnologyEntity capabilityTechnologyEntity = new CapabilityTechnologyEntity();
        capabilityTechnologyEntity.setTechnologyId(technologyId);
        return capabilityTechnologyEntity;
    }
}
