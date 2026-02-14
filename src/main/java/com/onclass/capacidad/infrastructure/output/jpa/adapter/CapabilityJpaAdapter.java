package com.onclass.capacidad.infrastructure.output.jpa.adapter;

import com.onclass.capacidad.application.dto.CapabilityWithTechnologies;
import com.onclass.capacidad.application.port.out.CapabilityRepositoryPort;
import com.onclass.capacidad.application.port.out.TechnologyCatalogPort;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityEntity;
import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityTechnologyEntity;
import com.onclass.capacidad.infrastructure.output.jpa.repository.CapabilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CapabilityJpaAdapter implements CapabilityRepositoryPort {

    private final CapabilityJpaRepository capabilityJpaRepository;
    private final TechnologyCatalogPort technologyCatalogPort;

    @Override
    public boolean existsByName(String name) {
        return capabilityJpaRepository.existsByName(name);
    }

    @Override
    public Capability save(Capability capability) {
        CapabilityEntity entity = new CapabilityEntity();
        entity.setName(capability.getName());
        entity.setDescription(capability.getDescription());

        Map<Long, String> technologyInfoMap = technologyCatalogPort.findTechnologiesByIds(new HashSet<>(capability.getTechnologyIds()));

        for (Long technologyId : capability.getTechnologyIds()) {
            CapabilityTechnologyEntity capabilityTechnologyEntity = new CapabilityTechnologyEntity();
            capabilityTechnologyEntity.setTechnologyId(technologyId);
            capabilityTechnologyEntity.setTechnologyName(technologyInfoMap.getOrDefault(technologyId, ""));
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
    public Page<CapabilityWithTechnologies> findAllWithTechnologyNames(Pageable pageable) {
        Page<CapabilityEntity> entities = capabilityJpaRepository.findAll(pageable);
        return entities.map(this::entityToWithTechnologyNames);
    }

    private Capability entityToDomain(CapabilityEntity entity) {
        List<Long> technologyIds = entity.getTechnologies().stream()
                .map(CapabilityTechnologyEntity::getTechnologyId)
                .collect(Collectors.toList());
        return Capability.rehydrate(entity.getId(), entity.getName(), entity.getDescription(), technologyIds);
    }

    private CapabilityWithTechnologies entityToWithTechnologyNames(CapabilityEntity entity) {
        List<CapabilityWithTechnologies.TechnologyInfo> techInfos = entity.getTechnologies().stream()
                .map(tech -> new CapabilityWithTechnologies.TechnologyInfo(
                        tech.getTechnologyId(),
                        tech.getTechnologyName()
                ))
                .toList();
        return new CapabilityWithTechnologies(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                techInfos
        );
    }
}
