package com.onclass.capacidad.infrastructure.mappers;

import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.infrastructure.entities.CapabilityEntity;
import org.springframework.stereotype.Component;

@Component
public class CapabilityEntityMapper {

    public CapabilityEntity toEntity(Capability capability) {
        if (capability == null) {
            return null;
        }

        CapabilityEntity entity = new CapabilityEntity();
        entity.setName(capability.getName());
        entity.setDescription(capability.getDescription());

        return entity;
    }

    public Capability toDomain(CapabilityEntity entity) {
        if (entity == null) {
            return null;
        }

        var technologyIds = entity.getTechnologies().stream()
                .map(tech -> tech.getTechnologyId())
                .toList();

        return Capability.rehydrate(entity.getId(), entity.getName(), entity.getDescription(), technologyIds);
    }
}
