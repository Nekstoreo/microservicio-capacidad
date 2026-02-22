package com.onclass.capacidad.infrastructure.adapters.persistence;

import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.infrastructure.entities.CapabilityEntity;
import com.onclass.capacidad.infrastructure.entities.CapabilityTechnologyEntity;
import com.onclass.capacidad.infrastructure.repositories.CapabilityJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CapabilityJpaAdapter implements CapabilityRepositoryPort {

    private final CapabilityJpaRepository capabilityJpaRepository;
    private final EntityManager entityManager;

    @Override
    public boolean existsByName(String name) {
        return capabilityJpaRepository.existsByName(name);
    }

    @Override
    public Capability save(Capability capability) {
        CapabilityEntity entity = new CapabilityEntity();
        entity.setName(capability.getName());
        entity.setDescription(capability.getDescription());

        for (Long technologyId : capability.getTechnologyIds()) {
            CapabilityTechnologyEntity capabilityTechnologyEntity = new CapabilityTechnologyEntity();
            capabilityTechnologyEntity.setTechnologyId(technologyId);
            entity.addTechnology(capabilityTechnologyEntity);
        }

        CapabilityEntity saved = capabilityJpaRepository.save(entity);
        List<Long> technologyIds = saved.getTechnologies().stream()
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .toList();

        return Capability.rehydrate(saved.getId(), saved.getName(), saved.getDescription(), technologyIds);
    }

    @Override
    public Capability findById(Long id) {
        return capabilityJpaRepository.findById(id)
                .map(this::entityToDomain)
                .orElse(null);
    }

    @Override
    public Page<Capability> findAll(Pageable pageable) {
        Page<CapabilityEntity> entities = capabilityJpaRepository.findAll(pageable);
        return entities.map(this::entityToDomain);
    }

    @Override
    public List<Capability> findAllByIds(List<Long> ids) {
        return capabilityJpaRepository.findAllById(ids).stream()
                .map(this::entityToDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        capabilityJpaRepository.deleteById(id);
    }

    @Override
    public Set<Long> findTechnologyReferences(Collection<Long> technologyIds) {
        if (technologyIds == null || technologyIds.isEmpty()) {
            return Set.of();
        }

        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT DISTINCT ct.technologyId FROM CapabilityTechnologyEntity ct WHERE ct.technologyId IN :ids",
                Long.class
        );
        query.setParameter("ids", technologyIds);

        return Set.copyOf(query.getResultList());
    }

    private Capability entityToDomain(CapabilityEntity entity) {
        List<Long> technologyIds = entity.getTechnologies().stream()
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .toList();
        return Capability.rehydrate(entity.getId(), entity.getName(), entity.getDescription(), technologyIds);
    }
}
