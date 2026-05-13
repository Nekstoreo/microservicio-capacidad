package com.onclass.capacidad.infrastructure.adapters.persistence;

import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.models.pagination.DomainPage;
import com.onclass.capacidad.domain.models.pagination.DomainPageRequest;
import com.onclass.capacidad.infrastructure.entities.CapabilityEntity;
import com.onclass.capacidad.infrastructure.entities.CapabilityTechnologyEntity;
import com.onclass.capacidad.infrastructure.repositories.CapabilityJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CapabilityJpaAdapterTest {

    @Mock
    private CapabilityJpaRepository repository;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CapabilityJpaAdapter adapter;

    @Test
    @DisplayName("Should return true if name exists")
    void shouldReturnTrueIfNameExists() {
        when(repository.existsByName("Java")).thenReturn(true);
        assertTrue(adapter.existsByName("Java"));
    }

    @Test
    @DisplayName("Should save capability")
    void shouldSaveCapability() {
        Capability capability = Capability.create("Java", "Desc", List.of(101L, 102L, 103L));
        CapabilityEntity savedEntity = new CapabilityEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Java");
        savedEntity.setDescription("Desc");

        CapabilityTechnologyEntity cte1 = new CapabilityTechnologyEntity();
        cte1.setTechnologyId(101L);
        CapabilityTechnologyEntity cte2 = new CapabilityTechnologyEntity();
        cte2.setTechnologyId(102L);
        CapabilityTechnologyEntity cte3 = new CapabilityTechnologyEntity();
        cte3.setTechnologyId(103L);
        savedEntity.setTechnologies(List.of(cte1, cte2, cte3));

        when(repository.save(any(CapabilityEntity.class))).thenReturn(savedEntity);

        Capability result = adapter.save(capability);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(3, result.getTechnologyIds().size());
        verify(repository).save(any(CapabilityEntity.class));
    }

    @Test
    @DisplayName("Should find capability by id")
    void shouldFindById() {
        CapabilityEntity entity = new CapabilityEntity();
        entity.setId(1L);
        entity.setName("Java");
        entity.setDescription("Desc");
        CapabilityTechnologyEntity cte1 = new CapabilityTechnologyEntity();
        cte1.setTechnologyId(101L);
        CapabilityTechnologyEntity cte2 = new CapabilityTechnologyEntity();
        cte2.setTechnologyId(102L);
        CapabilityTechnologyEntity cte3 = new CapabilityTechnologyEntity();
        cte3.setTechnologyId(103L);
        entity.setTechnologies(List.of(cte1, cte2, cte3));
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Capability result = adapter.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(3, result.getTechnologyIds().size());
    }

    @Test
    @DisplayName("Should return null when capability id not found")
    void shouldReturnNullWhenIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertNull(adapter.findById(1L));
    }

    @Test
    @DisplayName("Should find all capabilities by page")
    void shouldFindAllPage() {
        DomainPageRequest pageRequest = new DomainPageRequest(0, 10, "name", "asc");
        when(repository.findAll(any(org.springframework.data.domain.Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        DomainPage<Capability> result = adapter.findAll(pageRequest);

        assertNotNull(result);
        assertTrue(result.content().isEmpty());
    }

    @Test
    @DisplayName("Should delete capability by id")
    void shouldDeleteById() {
        adapter.deleteById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Should find technology references")
    @SuppressWarnings("unchecked")
    void shouldFindTechnologyReferences() {
        List<Long> ids = List.of(101L);
        TypedQuery<Long> query = mock(TypedQuery.class);

        when(entityManager.createQuery(anyString(), eq(Long.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(101L));

        Set<Long> result = adapter.findTechnologyReferences(ids);

        assertEquals(1, result.size());
        assertTrue(result.contains(101L));
    }
}
