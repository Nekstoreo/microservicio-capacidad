package com.onclass.capacidad.infrastructure.output.jpa.adapter;

import com.onclass.capacidad.application.port.out.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityEntity;
import com.onclass.capacidad.infrastructure.output.jpa.entity.CapabilityTechnologyEntity;
import com.onclass.capacidad.infrastructure.output.jpa.repository.CapabilityJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CapabilityJpaAdapter implements CapabilityRepositoryPort {

    private final CapabilityJpaRepository capabilityJpaRepository;

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
}
