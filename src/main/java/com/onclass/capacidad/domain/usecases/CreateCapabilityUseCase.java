package com.onclass.capacidad.domain.usecases;

import com.onclass.capacidad.application.dtos.commands.CreateCapabilityCommand;
import com.onclass.capacidad.domain.api.CreateCapabilityServicePort;
import com.onclass.capacidad.domain.exception.DuplicateCapabilityException;
import com.onclass.capacidad.domain.exception.TechnologiesNotFoundException;
import com.onclass.capacidad.domain.model.Capability;
import com.onclass.capacidad.domain.spi.CapabilityRepositoryPort;
import com.onclass.capacidad.domain.spi.TechnologyCatalogPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateCapabilityUseCase implements CreateCapabilityServicePort {

    private final CapabilityRepositoryPort capabilityRepositoryPort;
    private final TechnologyCatalogPort technologyCatalogPort;

    @Override
    public Capability execute(CreateCapabilityCommand command) {
        Capability capability = Capability.create(command.name(), command.description(), command.technologyIds());

        if (capabilityRepositoryPort.existsByName(capability.getName())) {
            throw new DuplicateCapabilityException(capability.getName());
        }

        Set<Long> requestedTechnologyIds = new LinkedHashSet<>(capability.getTechnologyIds());
        Set<Long> existingTechnologyIds = technologyCatalogPort.findExistingTechnologyIds(requestedTechnologyIds);

        List<Long> missingIds = capability.getTechnologyIds().stream()
                .filter(technologyId -> !existingTechnologyIds.contains(technologyId))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new TechnologiesNotFoundException(missingIds);
        }

        return capabilityRepositoryPort.save(capability);
    }
}
